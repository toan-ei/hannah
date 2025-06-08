package com.hannah.gateway_api.service;

import com.hannah.gateway_api.dto.request.CheckTokenRequest;
import com.hannah.gateway_api.dto.response.ApiResponse;
import com.hannah.gateway_api.dto.response.CheckTokenResponse;
import com.hannah.gateway_api.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;
    public Mono<ApiResponse<CheckTokenResponse>> checkToken(String token){
        return identityClient.checkToken(CheckTokenRequest.builder()
                        .token(token)
                .build());
    }
}
