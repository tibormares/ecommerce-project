package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }
        categoryRepository.deleteById(categoryId);
        return "Category with categoryId: '" + categoryId + "' deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(existingCategory);
            return savedCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

}
