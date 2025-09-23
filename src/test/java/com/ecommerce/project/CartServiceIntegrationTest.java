package com.ecommerce.project;

import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.*;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private AuthUtil authUtil;

    private Product product;
    private User user;

    @BeforeEach
    void setUpDatabase() {
        User userToSave = new User();
        userToSave.setEmail("test@example.com");
        userToSave.setUserName("test");
        userToSave.setPassword("password");

        this.user = userRepository.save(userToSave);

        Category category = new Category();
        category.setCategoryName("Electronics");
        category.setProducts(new ArrayList<>());
        Category savedCategory = categoryRepository.save(category);

        product = new Product();
        product.setProductName("Test Laptop");
        product.setDescription("A powerful laptop");
        product.setPrice(2000.0);
        product.setDiscount(10.0);
        product.setSpecialPrice(1800.0);
        product.setQuantity(50);
        product.setCategory(savedCategory);
        productRepository.save(product);
    }

    @Test
    void testAddProductToCart_shouldCreateCartAndAddItemForNewUser() {
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(authUtil.loggedInUser()).thenReturn(user);

        CartDTO resultCart = cartService.addProductToCart(product.getProductId(), 2);

        assertNotNull(resultCart);
        assertEquals(1, cartRepository.count());
        assertEquals(1, cartItemRepository.count());

        Cart cart = cartRepository.findAll().getFirst();
        CartItem cartItem = cartItemRepository.findAll().getFirst();

        assertEquals("test@example.com", cart.getUser().getEmail());
        assertEquals(3600.0, cart.getTotalPrice());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(product.getProductId(), cartItem.getProduct().getProductId());
    }
}
