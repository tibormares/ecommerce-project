package com.ecommerce.project;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.service.FileService;
import com.ecommerce.project.service.ProductServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private FileService fileService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImplementation productService;

    private Category category;
    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", new ArrayList<>());

        productDTO = new ProductDTO();
        productDTO.setProductName("Test Laptop");
        productDTO.setPrice(1000.0);
        productDTO.setDiscount(5.0);

        product = new Product();
        product.setProductId(101L);
        product.setProductName("Test Laptop");
        product.setPrice(1000.0);
        product.setDiscount(5.0);
    }

    @Test
    void addProduct_shouldCreateProduct_whenProductNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(productDTO, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductDTO savedProductDTO = productService.addProduct(1L, productDTO);

        assertNotNull(savedProductDTO);
        assertEquals("Test Laptop", savedProductDTO.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_shouldThrowException_whenProductAlreadyExists() {
        category.getProducts().add(product);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(APIException.class, () -> {
            productService.addProduct(1L, productDTO);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProducts_shouldReturnPagedProducts_whenProductsExist() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        ProductResponse result = productService.getAllProducts(0, 5, "price", "asc");

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Laptop", result.getContent().getFirst().getProductName());
    }

    @Test
    void getAllProducts_shouldThrowException_whenNoProductsExist() {
        when(productRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(APIException.class, () -> {
            productService.getAllProducts(0, 5, "price", "asc");
        });
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenProductExists() {
        ProductDTO updatedDetails = new ProductDTO();
        updatedDetails.setProductName("Updated Laptop Name");
        updatedDetails.setPrice(600.0);
        updatedDetails.setDiscount(5.0);

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(updatedDetails);

        ProductDTO result = productService.updateProduct(101L, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Laptop Name", result.getProductName());
        verify(productRepository, times(1)).findById(101L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(999L, productDTO);
        });
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenProductExists() {
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        Cart cart = new Cart();
        cart.setCartId(1L);
        when(cartRepository.findCartsByProductId(101L)).thenReturn(List.of(cart));

        ProductDTO deletedProduct = productService.deleteProduct(101L);

        assertNotNull(deletedProduct);
        verify(productRepository, times(1)).delete(product);

        verify(cartService, times(1)).deleteProductFromCart(1L, 101L);
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(999L);
        });
    }

}
