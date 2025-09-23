package com.ecommerce.project;

import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.repositories.*;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockitoBean
    private AuthUtil authUtil;

    private User user;
    private Address address;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUpDatabase() {
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

        User userToSave = new User();
        userToSave.setEmail("test@example.com");
        userToSave.setUserName("test");
        userToSave.setPassword("password");
        userToSave.setRoles(Set.of(userRole));
        this.user = userRepository.save(userToSave);

        Address addressToSave = new Address();
        addressToSave.setUser(user);
        addressToSave.setStreet("TestStreet 123");
        addressToSave.setCity("TestCity");
        addressToSave.setCountry("TestCountry");
        addressToSave.setZipcode("11111");
        addressToSave.setState("TestState");
        addressToSave.setBuildingName("TestBuildingName");
        this.address = addressRepository.save(addressToSave);

        Category category = new Category();
        category.setCategoryName("Books");
        category.setProducts(new ArrayList<>());
        category = categoryRepository.save(category);

        Product productToSave = new Product();
        productToSave.setProductName("Test Book");
        productToSave.setDescription("A book for testing");
        productToSave.setQuantity(50);
        productToSave.setPrice(250.0);
        productToSave.setDiscount(0.0);
        productToSave.setSpecialPrice(250.0);
        productToSave.setCategory(category);
        this.product = productRepository.save(productToSave);

        Cart cartToSave = new Cart();
        cartToSave.setUser(user);
        cartToSave.setCartItems(new ArrayList<>());
        this.cart = cartRepository.save(cartToSave);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setProductPrice(product.getSpecialPrice());

        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);

        cart.setTotalPrice(product.getSpecialPrice() * 2);
        this.cart = cartRepository.save(cart);
    }

    @Test
    void placeOrder_shouldPerformAllActionsInTransaction() {
        when(authUtil.loggedInEmail()).thenReturn(user.getEmail());

        OrderDTO resultOrder = orderService.placeOrder(user.getEmail(), address.getAddressId(), "CASH_ON_DELIVERY", "", "OK", "", "");

        assertNotNull(resultOrder);

        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
        assertEquals(500.0, orders.getFirst().getTotalAmount());
        assertEquals("Order Accepted", orders.getFirst().getOrderStatus());
        assertEquals(user.getEmail(), orders.getFirst().getEmail());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertEquals(1, orderItems.size());
        assertEquals(2, orderItems.getFirst().getQuantity());
        assertEquals(product.getProductId(), orderItems.getFirst().getProduct().getProductId());

        Product finalProductState = productRepository.findById(product.getProductId()).orElseThrow();
        assertEquals(48, finalProductState.getQuantity());

        assertEquals(0, cartItemRepository.count());
    }
}
