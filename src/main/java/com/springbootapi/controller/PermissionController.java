package com.springbootapi.controller;


import com.springbootapi.dto.request.PermissionRequest;
import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.service.permission_impl.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<DataResponse> permissions() {
        return ResponseEntity.status(HttpStatus.OK).body(
            DataResponse.builder()
                    .code("200")
                    .message("Query Successfully")
                    .timestamp(LocalDateTime.now())
                    .data(permissionService.getAllPermissions())
                    .build()
        );
    }
    @PostMapping
    public ResponseEntity<DataResponse> addPermission(@RequestBody PermissionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                DataResponse.builder()
                        .code("201")
                        .message("Permission Created")
                        .timestamp(LocalDateTime.now())
                        .data(permissionService.createPermission(request))
                        .build()
        ) ;
    }
}
