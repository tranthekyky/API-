package com.springbootapi.service.product_impl;


import com.springbootapi.dto.PageResponse;
import com.springbootapi.model.Product;
import com.springbootapi.repository.IProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final IProductRepository productRepository ;


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
       return productRepository.save(product);
    }

    @Override
    public void update(Product product, Integer id) {
        Product product1 = getProduct(id);
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        productRepository.save(product1);
    }

    @Override
    public void delete(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }else {
            throw new EntityNotFoundException("ID : " + id + " not found");
        }

    }
    public Product getProduct(Integer id) {
        return productRepository.findById(id).orElseThrow(() ->new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public PageResponse getAllProducts(int page, int size , String sortBy , String s , int categoryId , double minPrice ,
                                       double maxPrice) {
        Specification<Product> spec = Specification.where(null);
        if (s != null && !s.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + s + "%"));
        }
        if (categoryId != 0) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        }
        if (minPrice != 0) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != 0) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }


        String [] strings = sortBy.split(",");
        Sort sort = Sort.unsorted();
        for (String str : strings) {
            String[] substring = str.split(":");
            if (substring.length == 2) {
                String field = substring[0];
                String direction = substring[1];
                if (direction.equalsIgnoreCase("asc")) {
                    sort = sort.and(Sort.by(Sort.Direction.ASC,field)) ;
                }else if (direction.equalsIgnoreCase("desc")) {
                    sort = sort.and(Sort.by(Sort.Direction.DESC, field));
                }
            }else {
                throw new IllegalArgumentException("Invalid sort format" + s);
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(spec,pageable);
        return PageResponse.builder()
                .page(page)
                .size(size)
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .data(productPage.getContent())
                .build();
    }
}
