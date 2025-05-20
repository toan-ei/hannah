package com.hannah.identity_service.config;

import com.hannah.identity_service.dto.request.CheckTokenRequest;
import com.hannah.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    private final AuthenticationService authenticationService;

    @Value("${jwt.signerKey}")
    String signerKey;

    public CustomJwtDecoder(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.checkToken(CheckTokenRequest.builder().token(token).build());
            if(!response.isValid()){
                throw new JwtException("Token invalid");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        NimbusJwtDecoder nimbusJwtDecoder = null;
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec spec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
