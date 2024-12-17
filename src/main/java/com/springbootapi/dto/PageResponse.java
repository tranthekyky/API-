package com.springbootapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Object data ;
}
