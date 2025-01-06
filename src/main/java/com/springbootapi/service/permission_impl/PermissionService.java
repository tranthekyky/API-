package com.springbootapi.service.permission_impl;


import com.springbootapi.dto.request.PermissionRequest;
import com.springbootapi.model.Permission;
import com.springbootapi.repository.IPermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PermissionService {
    IPermissionRepository permissionRepository;


    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
    public Permission createPermission(PermissionRequest request) {
        Permission permission = Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build() ;
        return permissionRepository.save(permission);
    }
    public void deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }

}
