package com.springbootapi.service;

import com.springbootapi.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IGenerateService <T>{
    List<T> findAll(); ;
    T save(T t) ;
    void update(T t , Integer id) ;
    void delete(Integer id) ;
}
