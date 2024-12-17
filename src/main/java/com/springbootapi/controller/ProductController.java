package com.springbootapi.controller;


import com.springbootapi.dto.DataResponse;
import com.springbootapi.dto.PageResponse;
import com.springbootapi.model.Product;
import com.springbootapi.service.product_impl.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllProducts(@RequestParam(name = "pageNum",defaultValue = "0" , required = false) int pageNum ,
                                                       @RequestParam(name = "size",defaultValue = "3" , required = false) int size ,
                                                       @RequestParam(name = "sortBy", defaultValue = "id:asc" , required = false) String sortBy ,
                                                       @RequestParam(name = "s" ,defaultValue = "", required = false) String s ,
                                                       @RequestParam(name = "categoryId" ,defaultValue = "0" , required = false) int categoryId ,
                                                       @RequestParam(name = "min" ,defaultValue = "0", required = false) double min ,
                                                       @RequestParam(name = "max" ,defaultValue = "0", required = false) double max) {

        log.info("Get all products with page {} and size {}", pageNum, size);
        PageResponse products = productService.getAllProducts(pageNum, size , sortBy , s , categoryId , min , max);
        if (products.getTotalElements() == 0 ) {
            return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                    "200", "Query successful" , HttpStatus.OK, LocalDateTime.now() , "No matching results !!"
            )) ;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                "200", "Query successful" , HttpStatus.OK, LocalDateTime.now() , products
        )) ;
    }
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                "200","Query Product successful with id : " + id , HttpStatus.OK, LocalDateTime.now() , productService.getProduct(id)
        ));
    }
    @PostMapping
    public ResponseEntity<DataResponse> addProduct(@RequestBody @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse(
                "200" , "Query successful" , HttpStatus.CREATED , LocalDateTime.now(), productService.save(product)
        )) ;
    }
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Integer id,
                                                      @RequestBody @Valid Product product) {
        productService.update(product, id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                "201" , "Update Successfully with id : " + id , HttpStatus.OK, LocalDateTime.now() , productService.getProduct(id)
        ));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse(
                "201" , "Delete Successful with id : " + id  , HttpStatus.OK , LocalDateTime.now() , ""
        ));
    }
}
