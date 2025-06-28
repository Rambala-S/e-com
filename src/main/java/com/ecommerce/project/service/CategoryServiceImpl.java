package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repo.CategoriesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    //private List<Category> categories = new ArrayList<>();
    //long nextId = 1;

    @Autowired
    CategoriesRepository categoriesRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoriesRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No Categories are present");
            List<CategoryDto> categoryDto = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDto);
            return categoryResponse;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        //category.setCategoryId(nextId++);
        Category category = modelMapper.map(categoryDto, Category.class);
        Category categoryFromDB = categoriesRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null) {
            throw new APIException("Categories with this name "+category.getCategoryName()+ " already exists");
        }
        Category savedCategory = categoriesRepository.save(category);
        CategoryDto savedCategoryDto = modelMapper.map(savedCategory, CategoryDto.class);
        return savedCategoryDto;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        categoriesRepository.delete(category);
        return "Category Deleted";

    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category existingCategory = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        existingCategory.setCategoryName(category.getCategoryName());

        return categoriesRepository.save(existingCategory);
    }



}
