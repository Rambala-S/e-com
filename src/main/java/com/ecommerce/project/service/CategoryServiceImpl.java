package com.ecommerce.project.service;

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
        return categoriesRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        //category.setCategoryId(nextId++);
        categoriesRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Resoure not Found"));
        categoriesRepository.delete(category);
        return "Category Deleted";

    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category existingCategory = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Resoure not Found"));
        existingCategory.setCategoryName(category.getCategoryName());
        existingCategory.setEmail(category.getEmail());

        return categoriesRepository.save(existingCategory);
    }



}
