package com.hannah.identity_service.mapper;

import com.hannah.identity_service.dto.request.CreateUserRequest;
import com.hannah.identity_service.dto.request.ProfileRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ProfileMapper {
    ProfileRequest toProfileRequest(CreateUserRequest createUserRequest);
}
