package com.springbootapi.service.category_impl;

import com.springbootapi.model.Category;
import com.springbootapi.repository.ICategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    @Override
    public List<Category> findAll() {
       return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void update(Category category, Integer id) {
        if (categoryRepository.findById(id).isPresent()) {
            Category category1 = Category.builder()
                    .id(id)
                    .name(category.getName())
                    .build();
            categoryRepository.save(category1);
        }else {
            throw new EntityNotFoundException("Category not found !");
        }
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
    public Category getCateById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Category with id " + id + " not found !"));
    }
}
