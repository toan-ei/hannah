package com.hannah.gateway_api.repository;

import com.hannah.gateway_api.dto.request.CheckTokenRequest;
import com.hannah.gateway_api.dto.response.ApiResponse;
import com.hannah.gateway_api.dto.response.CheckTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/checkToken", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<CheckTokenResponse>> checkToken(@RequestBody CheckTokenRequest checkTokenRequest);
}
