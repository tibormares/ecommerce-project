package com.ecommerce.project;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.service.CategoryServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplementationTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImplementation categoryService;

    @Test
    void getAllCategories_shouldReturnPagedCategories_whenCategoriesExist() {
        Integer pageNumber = 0;
        Integer pageSize = 5;
        String sortBy = "categoryName";
        String sortOrder = "asc";

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        List<Category> categories = List.of(new Category(1L, "Electronics"), new Category(2L, "Books"));

        Page<Category> categoryPage = new PageImpl<>(categories, pageDetails, categories.size());

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(modelMapper.map(any(Category.class), eq(CategoryDTO.class)))
                .thenAnswer(invocation -> {
                    Category source = invocation.getArgument(0);
                    return new CategoryDTO(source.getCategoryId(), source.getCategoryName());
                });

        CategoryResponse response = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("Electronics", response.getContent().getFirst().getCategoryName());
        assertEquals(pageNumber, response.getPageNumber());
        assertEquals(pageSize, response.getPageSize());
    }

    @Test
    void getAllCategories_shouldThrowException_whenNoCategoriesExist() {
        Integer pageNumber = 0;
        Integer pageSize = 5;
        String sortBy = "categoryName";
        String sortOrder = "asc";

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(APIException.class, () ->
                categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder)
        );
    }

    @Test
    void createCategory_shouldCreateCategory_whenCategoryNameIsUnique() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Kitchen");

        Category category = new Category();
        category.setCategoryName("Kitchen");

        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.findByCategoryName(category.getCategoryName())).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);

        verify(categoryRepository, times(1)).save(category);
        assertEquals(categoryDTO, createdCategoryDTO);
    }

    @Test
    void createCategory_shouldThrowException_whenCategoryNameIsPresent() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Kitchen");

        Category category = new Category();
        category.setCategoryName("Kitchen");

        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.findByCategoryName(category.getCategoryName())).thenReturn(category);

        assertThrows(APIException.class, () ->
                categoryService.createCategory(categoryDTO)
        );
    }

    @Test
    void deleteCategory_shouldDeleteCategory_whenCategoryExists() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Electronics");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(new CategoryDTO());

        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).delete(category);
        assertNotNull(deletedCategoryDTO);
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryNotFound() {
        Long nonExistentCategoryId = 999L;

        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteCategory(nonExistentCategoryId)
        );
    }

    @Test
    void updateCategory_shouldUpdateCategory_whenCategoryFound() {
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("New Name");

        Category categoryBeforeUpdate = new Category();
        categoryBeforeUpdate.setCategoryName("Old Name");
        categoryBeforeUpdate.setCategoryId(categoryId);

        Category categoryAfterUpdate = new Category();
        categoryAfterUpdate.setCategoryName("New Name");
        categoryAfterUpdate.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryBeforeUpdate));
        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(categoryAfterUpdate);
        when(categoryRepository.save(categoryBeforeUpdate)).thenReturn(categoryAfterUpdate);
        when(modelMapper.map(categoryAfterUpdate, CategoryDTO.class)).thenReturn(categoryDTO);

        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);

        verify(categoryRepository, times(1)).save(categoryBeforeUpdate);
        assertEquals("New Name", savedCategoryDTO.getCategoryName());
    }

    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Garage");

        Long nonExistentCategoryId = 999L;

        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.updateCategory(categoryDTO, nonExistentCategoryId)
        );
    }

}
