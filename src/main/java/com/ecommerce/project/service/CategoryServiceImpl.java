package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repo.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    //private List<Category> categories = new ArrayList<>();
    //long nextId = 1;

    @Autowired
    CategoriesRepository categoriesRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoriesRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No Categories are present");
            return categories;
    }

    @Override
    public void createCategory(Category category) {
        //category.setCategoryId(nextId++);
        Category savedCategory = categoriesRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new APIException("Categories with this name "+category.getCategoryName()+ " already exists");
        }
        categoriesRepository.save(category);
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
