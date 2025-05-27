package com.hannah.profile_service.controller;

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
@RequestMapping("/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping("/createProfile")
    public ApiResponse<ProfileResponse> createProfile(@RequestBody ProfileRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.createProfile(request))
                .build();
    }

    @GetMapping("/getProfile/{profileId}")
    public ApiResponse<ProfileResponse> getProfile(@PathVariable String profileId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfile(profileId))
                .build();
    }

    @GetMapping("/GetAllProfile")
    public ApiResponse<List<ProfileResponse>> getAllProfile(){
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfile())
                .build();
    }

    @PutMapping("/updateProfile/{profileId}")
    public ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest request,
                                                      @PathVariable String profileId){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.updateProfile(request, profileId))
                .build();
    }

    @DeleteMapping("/deleteProfile/{profileId}")
    public void deleteProfile(@PathVariable String profileId){
        profileService.deleteProfile(profileId);
    }
}
