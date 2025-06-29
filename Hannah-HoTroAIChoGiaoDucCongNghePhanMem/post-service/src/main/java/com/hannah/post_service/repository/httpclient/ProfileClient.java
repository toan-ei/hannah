package com.hannah.post_service.repository.httpclient;

import com.hannah.post_service.config.FeignConfiguration;
import com.hannah.post_service.dto.request.ProfileListUserIDRequest;
import com.hannah.post_service.dto.response.ApiResponse;
import com.hannah.post_service.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile", configuration = FeignConfiguration.class)
public interface ProfileClient {
    @GetMapping(value = "/profiles/getProfile/fromUserId/{userId}")
    ApiResponse<ProfileResponse> getProfileFromUserId(@PathVariable String userId);
    @PostMapping(value = "/profiles/Internal/fromUserIds",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<ProfileResponse>> getProfilesFromUserIds(@RequestBody ProfileListUserIDRequest request);
}
