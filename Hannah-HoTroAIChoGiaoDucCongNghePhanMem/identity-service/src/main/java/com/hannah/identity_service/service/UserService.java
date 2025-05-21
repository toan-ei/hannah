package com.hannah.identity_service.service;

import com.hannah.identity_service.constant.PredefinedRole;
import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.UpdateUserRequest;
import com.hannah.identity_service.dto.response.UserResponse;
import com.hannah.identity_service.entity.Role;
import com.hannah.identity_service.entity.User;
import com.hannah.identity_service.exception.ApplicationException;
import com.hannah.identity_service.exception.ErrorCode;
import com.hannah.identity_service.mapper.UserMapper;
import com.hannah.identity_service.repository.RoleRepository;
import com.hannah.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

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
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
