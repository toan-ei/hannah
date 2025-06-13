package com.hannah.profile_service.controller.internal;

import com.hannah.profile_service.dto.request.ProfileListUserIDRequest;
import com.hannah.profile_service.dto.request.ProfileRequest;
import com.hannah.profile_service.dto.request.ProfileUpdateRequest;
import com.hannah.profile_service.dto.response.ApiResponse;
import com.hannah.profile_service.dto.response.ProfileResponse;
import com.hannah.profile_service.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalProfileController {
    ProfileService profileService;

    @PostMapping("/profiles/Internal/createProfile")
    public ApiResponse<ProfileResponse> createProfile(@RequestBody ProfileRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createProfile(request))
                .build();
    }

    @PostMapping("/profiles/Internal/fromUserIds")
    public ApiResponse<List<ProfileResponse>> getProfilesFromUserIds(@RequestBody ProfileListUserIDRequest request) {
        List<ProfileResponse> profiles = profileService.getProfilesFromUserIds(request);
        log.info("profile {}", profiles);
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profiles)
                .build();
    }
}
