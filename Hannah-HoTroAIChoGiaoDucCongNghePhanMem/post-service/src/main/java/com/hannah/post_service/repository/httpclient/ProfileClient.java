package com.hannah.post_service.repository.httpclient;

import com.hannah.post_service.config.AuthenticationRequestInterceptor;
import com.hannah.post_service.dto.response.ApiResponse;
import com.hannah.post_service.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")
public interface ProfileClient {
    @GetMapping(value = "/profiles/getProfile/fromUserId/{userId}")
    ApiResponse<ProfileResponse> getProfileFromUserId(@PathVariable String userId);
}
