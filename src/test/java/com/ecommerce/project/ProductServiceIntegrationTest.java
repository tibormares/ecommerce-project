package com.ecommerce.project;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.CategoryService;
import com.ecommerce.project.service.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testAddProduct_shouldCreateAndLinkProductToCategory() {
        Category category = new Category();
        category.setCategoryName("Electronics");

        category.setProducts(new ArrayList<>());

        Category savedCategory = categoryRepository.save(category);
        assertNotNull(savedCategory.getCategoryId());

        ProductDTO productToCreate = new ProductDTO();
        productToCreate.setProductName("Test Laptop");
        productToCreate.setDescription("This is a test product description.");
        productToCreate.setPrice(1200.0);
        productToCreate.setDiscount(10.0);

        ProductDTO savedProduct = productService.addProduct(savedCategory.getCategoryId(), productToCreate);

        assertNotNull(savedProduct.getProductId());
        assertEquals("Test Laptop", savedProduct.getProductName());
        assertEquals(1, productRepository.count());

        Product foundProduct = productRepository.findById(savedProduct.getProductId()).orElseThrow();
        assertEquals("Electronics", foundProduct.getCategory().getCategoryName());
    }

}
