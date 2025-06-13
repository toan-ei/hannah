package com.hannah.identity_service.service;

import com.hannah.identity_service.constant.PredefinedRole;
import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.ProfileListUserIDRequest;
import com.hannah.identity_service.dto.request.ProfileRequest;
import com.hannah.identity_service.dto.request.UpdateUserRequest;
import com.hannah.identity_service.dto.response.ApiResponse;
import com.hannah.identity_service.dto.response.ProfileResponse;
import com.hannah.identity_service.dto.response.UserResponse;
import com.hannah.identity_service.entity.Role;
import com.hannah.identity_service.entity.User;
import com.hannah.identity_service.exception.ApplicationException;
import com.hannah.identity_service.exception.ErrorCode;
import com.hannah.identity_service.mapper.ProfileMapper;
import com.hannah.identity_service.mapper.UserMapper;
import com.hannah.identity_service.repository.RoleRepository;
import com.hannah.identity_service.repository.UserRepository;
import com.hannah.identity_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new ApplicationException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.STUDENT_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        user = userRepository.save(user);
        ProfileRequest profileRequest = profileMapper.toProfileRequest(request);
        profileRequest.setUserId(user.getId());
        profileClient.createProfile(profileRequest);
        return userMapper.toUserResponse(user);
    }
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId){
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> getAllUser(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse updateUser(UpdateUserRequest request, String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public List<ProfileResponse> getUserRoleTeacher(){
        List<User> allUser = userRepository.findAll();
        List<String> userId = new ArrayList<>();
        allUser.forEach(user -> {
            user.getRoles().forEach(role -> {
                if(role.getName().equals(PredefinedRole.TEACHER_ROLE)){
                    userId.add(user.getId());
                }
            });
        });
        log.info("userids {}", userId);
        ProfileListUserIDRequest profileListUserIDRequest = ProfileListUserIDRequest.builder()
                .userIds(userId)
                .build();
        try {
            ApiResponse<List<ProfileResponse>> profilesFromUserIds = profileClient.getProfilesFromUserIds(profileListUserIDRequest);
            return profilesFromUserIds.getResult();
        } catch (Exception e) {
            log.error("Lỗi gọi profileClient", e);
            throw new ApplicationException(ErrorCode.valueOf("loi"));
        }
    }
}
