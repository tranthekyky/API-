package com.springbootapi.controller;


import com.springbootapi.dto.response.DataResponse;
import com.springbootapi.service.category_impl.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code("200")
                        .status(HttpStatus.OK)
                        .message("Query Successful !")
                        .timestamp(LocalDateTime.now())
                        .data(categoryService.findAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                DataResponse.builder()
                        .code("200")
                        .status(HttpStatus.OK)
                        .message("Query Successful with id " + id)
                        .timestamp(LocalDateTime.now())
                        .data(categoryService.getCateById(id))
                        .build()
        );
    }

}
