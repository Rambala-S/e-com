package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDto createCategory(CategoryDto categoryDto);

    String deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
