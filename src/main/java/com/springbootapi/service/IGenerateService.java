package com.springbootapi.service;

import java.util.List;

public interface IGenerateService <T>{
    List<T> findAll(); ;
    T save(T t) ;
    void update(T t , Integer id) ;
    void delete(Integer id) ;
}
