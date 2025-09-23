package com.ecommerce.project;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.repositories.*;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.service.OrderServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImplementation orderService;

    private Cart cart;
    private Address address;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(101L);
        product.setQuantity(20);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setCartId(1L);
        cart.setTotalPrice(500.0);
        cart.setCartItems(List.of(cartItem));

        address = new Address();
        address.setAddressId(1L);
    }

    @Test
    void placeOrder_shouldCreateOrder_whenCartAndAddressExist() {
        String email = "test@example.com";
        Long addressId = 1L;

        when(cartRepository.findCartByEmail(email)).thenReturn(cart);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(123L);
            return order;
        });
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO orderDTOWithInitializedList = new OrderDTO();
        orderDTOWithInitializedList.setOrderItems(new ArrayList<>());
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTOWithInitializedList);

        orderService.placeOrder(email, addressId, "CARD", "pg123", "OK", "", "");

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals(18, savedProduct.getQuantity());

        verify(cartService, times(1)).deleteProductFromCart(cart.getCartId(), product.getProductId());
    }

    @Test
    void placeOrder_shouldThrowException_whenCartNotFound() {
        when(cartRepository.findCartByEmail(anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.placeOrder("user@example.com", 1L, "CARD", "", "", "", "");
        });
    }

    @Test
    void placeOrder_shouldThrowException_whenAddressNotFound() {
        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.placeOrder("user@example.com", 999L, "CARD", "", "", "", "");
        });
    }

    @Test
    void placeOrder_shouldThrowException_whenCartIsEmpty() {
        cart.setCartItems(new ArrayList<>());

        when(cartRepository.findCartByEmail(anyString())).thenReturn(cart);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        assertThrows(APIException.class, () -> {
            orderService.placeOrder("user@example.com", 1L, "CARD", "", "", "", "");
        });
    }
}