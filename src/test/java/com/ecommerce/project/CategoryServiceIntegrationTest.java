package com.ecommerce.project;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.service.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateCategory_And_ThenFindIt() {
        CategoryDTO categoryToCreate = new CategoryDTO();
        categoryToCreate.setCategoryName("Integration Test Category");

        CategoryDTO savedCategory = categoryService.createCategory(categoryToCreate);

        assertNotNull(savedCategory.getCategoryId());
        assertEquals(1, categoryRepository.count());

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getCategoryId());
        assertTrue(foundCategory.isPresent());
        assertEquals("Integration Test Category", foundCategory.get().getCategoryName());
    }

}
