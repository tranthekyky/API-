package com.springbootapi.controller;


import com.springbootapi.dto.request.AuthenticateRequest;
import com.springbootapi.dto.request.IntrospectRequest;
import com.springbootapi.dto.response.AuthenticateResponse;
import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.dto.response.IntrospectResponse;
import com.springbootapi.service.user_impl.AuthenticateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthenticateController {
    AuthenticateService authenticateService;

    @PostMapping("/getToken")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest authenticateRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                authenticateService.authenticate(authenticateRequest)
        );
    }
    @PostMapping("/introspect-token")
    public ResponseEntity<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest introspectRequest) {
        var result = authenticateService.introspectToken(introspectRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
