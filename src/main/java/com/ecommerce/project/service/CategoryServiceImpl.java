package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repo.CategoriesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoriesRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();

        if(categories.isEmpty())
            throw new APIException("No Categories are present");
            List<CategoryDto> categoryDto = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDto);

            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setLastPage(categoryPage.isLast());
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
    public CategoryDto deleteCategory(Long categoryId) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        CategoryDto deletedCategory = modelMapper.map(category,CategoryDto.class);
        categoriesRepository.delete(category);
        return deletedCategory;

    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {

        Category category = modelMapper.map(categoryDto,Category.class);
        Category existingCategory = categoriesRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        existingCategory.setCategoryName(category.getCategoryName());

        categoriesRepository.save(existingCategory);
        CategoryDto savedCategory = modelMapper.map(existingCategory,CategoryDto.class);
        return savedCategory;
    }



}
