package com.hannah.identity_service.service;

import com.hannah.identity_service.dto.request.AuthenticationRequest;
import com.hannah.identity_service.dto.request.CheckTokenRequest;
import com.hannah.identity_service.dto.response.AuthenticationResponse;
import com.hannah.identity_service.dto.response.CheckTokenResponse;
import com.hannah.identity_service.entity.User;
import com.hannah.identity_service.exception.ApplicationException;
import com.hannah.identity_service.exception.ErrorCode;
import com.hannah.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;
    @NonFinal
    @Value("${jwt.valid-duration}")
    int validDuration;

    public AuthenticationResponse authenticated(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new ApplicationException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .valid(true)
                .token(token)
                .build();
    }

    public CheckTokenResponse checkToken(CheckTokenRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try{
            verifyToken(token);
        }
        catch (ApplicationException e){
            isValid = false;
        }
        return CheckTokenResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean expireTime = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
        var verifier = signedJWT.verify(jwsVerifier);
        if(!(verifier && expireTime)){
            throw new ApplicationException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .issuer("em toan nghien code")
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "coming soon")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
