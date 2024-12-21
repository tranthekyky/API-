package com.springbootapi.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Builder
@Data
public class JsonResponse {
    private HttpStatus status;
    private String message;

}
