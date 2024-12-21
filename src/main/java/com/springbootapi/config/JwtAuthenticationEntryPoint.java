package com.springbootapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.dto.response.ErrorResponse;
import com.springbootapi.dto.response.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        JsonResponse jsonResponse = JsonResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("You do not have permission to access this resource!")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        response.flushBuffer();
    }
}