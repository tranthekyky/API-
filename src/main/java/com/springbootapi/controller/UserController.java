package com.springbootapi.controller;


import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.model.User;
import com.springbootapi.service.user_impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<DataResponse> createUser (@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                DataResponse.builder()
                        .code(HttpStatus.CREATED.getReasonPhrase())
                        .status(HttpStatus.CREATED)
                        .message("User created successfully")
                        .timestamp(LocalDateTime.now())
                        .data(userService.save(user))
                        .build()
        );
    }
    @GetMapping
    public ResponseEntity<DataResponse> getAllUsers () {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code(HttpStatus.OK.getReasonPhrase())
                        .status(HttpStatus.OK)
                        .message("Query Successfully !")
                        .timestamp(LocalDateTime.now())
                        .data(userService.findAll())
                        .build()
        ) ;
    }
    @GetMapping ("/info")
    public ResponseEntity<DataResponse> getInFoUser () {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code(HttpStatus.OK.getReasonPhrase())
                        .status(HttpStatus.OK)
                        .message("Query Successfully !")
                        .timestamp(LocalDateTime.now())
                        .data(userService.getInfoUser())
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getUserById (@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code(HttpStatus.OK.getReasonPhrase())
                        .status(HttpStatus.OK)
                        .message("Query Successfully !")
                        .timestamp(LocalDateTime.now())
                        .data(userService.findById(id))
                        .build()
        );
    }
}
