package com.springbootapi.controller;


import com.springbootapi.dto.request.RoleRequest;
import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.service.role_impl.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<DataResponse> GetAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code("124")
                        .message("Query success")
                        .timestamp(LocalDateTime.now())
                        .data(roleService.getAllRoles())
                        .build()
        );
    }
    @PostMapping
    public ResponseEntity<DataResponse> CreateRole(@RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                DataResponse.builder()
                        .code("200")
                        .message("Create success")
                        .timestamp(LocalDateTime.now())
                        .data(roleService.createRole(request))
                        .build()
        );
    }
}
