package com.hannah.identity_service.mapper;

import com.hannah.identity_service.dto.request.PermissionRequest;
import com.hannah.identity_service.dto.response.PermissionResponse;
import com.hannah.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
