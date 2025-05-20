package com.hannah.identity_service.mapper;

import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.UpdateUserRequest;
import com.hannah.identity_service.dto.response.UserResponse;
import com.hannah.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    User toUser(CreateUserRequest request);
    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);
}
