package com.springbootapi.service.product_impl;

import com.springbootapi.dto.response.PageResponse;
import com.springbootapi.model.Product;
import com.springbootapi.service.IGenerateService;


public interface IProductService extends IGenerateService<Product> {
    PageResponse getAllProducts(int page, int size  , String sort ,
                                 String s , int categoryId , double minPrice , double maxPrice);
}
