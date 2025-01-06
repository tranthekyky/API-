package com.springbootapi.service.role_impl;


import com.springbootapi.dto.request.RoleRequest;
import com.springbootapi.model.Permission;
import com.springbootapi.model.Role;
import com.springbootapi.repository.IPermissionRepository;
import com.springbootapi.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RoleService {
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public Role createRole(RoleRequest request) {
        var permissions = permissionRepository.findAllById(request.getPermissions());
        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(new HashSet<>(permissions))
                .build();
        return roleRepository.save(role);
    }
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }

}
