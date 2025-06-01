package com.hannah.profile_service.controller.internal;

import com.hannah.profile_service.dto.request.ProfileRequest;
import com.hannah.profile_service.dto.request.ProfileUpdateRequest;
import com.hannah.profile_service.dto.response.ApiResponse;
import com.hannah.profile_service.dto.response.ProfileResponse;
import com.hannah.profile_service.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles/Internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    ProfileService profileService;

    @PostMapping("/createProfile")
    public ApiResponse<ProfileResponse> createProfile(@RequestBody ProfileRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createProfile(request))
                .build();
    }

    @GetMapping("/getProfile/{userId}")
    public ApiResponse<ProfileResponse> getProfileFromUserId(@PathVariable String userId){
        ProfileResponse profileResponse = profileService.getProfileFromUserId(userId);
        return ApiResponse.<ProfileResponse>builder()
                .result(profileResponse)
                .build();
    }


}
