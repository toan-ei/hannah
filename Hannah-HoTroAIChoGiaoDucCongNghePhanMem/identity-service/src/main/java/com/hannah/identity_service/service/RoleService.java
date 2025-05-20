package com.hannah.identity_service.service;

import com.hannah.identity_service.dto.request.RoleRequest;
import com.hannah.identity_service.dto.response.RoleResponse;
import com.hannah.identity_service.entity.Role;
import com.hannah.identity_service.exception.ApplicationException;
import com.hannah.identity_service.exception.ErrorCode;
import com.hannah.identity_service.mapper.RoleMapper;
import com.hannah.identity_service.repository.PermissionRepository;
import com.hannah.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse createRole(RoleRequest request){
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAllRole(){
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    public String deleteRole(String nameRole){
        Role role = roleRepository.findById(nameRole)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        roleRepository.delete(role);
        return "delete role success";
    }
}
