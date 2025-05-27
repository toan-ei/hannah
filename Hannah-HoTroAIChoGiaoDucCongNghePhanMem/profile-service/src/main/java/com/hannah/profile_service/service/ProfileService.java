package com.hannah.profile_service.service;

import com.hannah.profile_service.dto.request.ProfileRequest;
import com.hannah.profile_service.dto.request.ProfileUpdateRequest;
import com.hannah.profile_service.dto.response.ProfileResponse;
import com.hannah.profile_service.entity.Profile;
import com.hannah.profile_service.exception.ApplicationException;
import com.hannah.profile_service.exception.ErrorCode;
import com.hannah.profile_service.mapper.ProfileMapper;
import com.hannah.profile_service.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public ProfileResponse createProfile(ProfileRequest request){
        Profile profile = profileMapper.toProfile(request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public ProfileResponse getProfile(String id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
        return profileResponse;
    }

    public List<ProfileResponse> getAllProfile(){
        return profileRepository.findAll()
                .stream().map(profileMapper::toProfileResponse).collect(Collectors.toList());
    }

    public ProfileResponse updateProfile(ProfileUpdateRequest request, String id){
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        profileMapper.updateProfile(profile, request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public void deleteProfile(String id){
        profileRepository.deleteById(id);
    }
}
