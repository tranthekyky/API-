package com.springbootapi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Integer status;
    private String errorMessage;
    private List<String> message;
    private LocalDateTime timestamp;
    private String path;

}
