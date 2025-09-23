package com.ecommerce.project;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.CartServiceImplementation;
import com.ecommerce.project.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartServiceImplementation cartService;

    private User user;
    private Cart cart;
    private Product product;
    private CartDTO cartDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");

        cart = new Cart();
        cart.setCartId(1L);
        cart.setUser(user);
        cart.setTotalPrice(0.0);

        product = new Product();
        product.setProductId(101L);
        product.setProductName("Test Product");
        product.setQuantity(5);
        product.setSpecialPrice(100.0);

        cartDTO = new CartDTO();
        cartDTO.setCartId(1L);
    }

    @Test
    void addProductToCart_shouldAddItem_whenCartExistsAndProductIsNotInCart() {
        int quantityToAdd = 2;

        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail("test@example.com")).thenReturn(cart);

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));

        when(cartItemRepository.findCartItemByProductIdAndCartId(101L, 1L)).thenReturn(null);

        when(modelMapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(cartDTO);

        CartDTO resultCart = cartService.addProductToCart(101L, quantityToAdd);

        assertNotNull(resultCart);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(cart);
        assertEquals(200.0, cart.getTotalPrice());
    }

    @Test
    void addProductToCart_shouldThrowException_whenProductAlreadyInCart() {
        int quantityToAdd = 1;

        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail("test@example.com")).thenReturn(cart);

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));

        when(cartItemRepository.findCartItemByProductIdAndCartId(101L, 1L)).thenReturn(new CartItem());

        assertThrows(APIException.class, () -> {
            cartService.addProductToCart(101L, quantityToAdd);
        });

        verify(cartItemRepository, never()).save(any(CartItem.class));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void addProductToCart_shouldCreateNewCart_whenUserHasNoCart() {
        int quantityToAdd = 1;
        when(authUtil.loggedInEmail()).thenReturn("newuser@example.com");
        when(authUtil.loggedInUser()).thenReturn(user);

        when(cartRepository.findCartByEmail("newuser@example.com")).thenReturn(null);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(anyLong(), anyLong())).thenReturn(null);
        when(modelMapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(cartDTO);

        cartService.addProductToCart(101L, quantityToAdd);

        verify(cartRepository, times(2)).save(any(Cart.class));
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addProductToCart_shouldThrowException_whenProductIsOutOfStock() {
        product.setQuantity(0);
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail("test@example.com")).thenReturn(cart);
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));

        assertThrows(APIException.class, () -> {
            cartService.addProductToCart(101L, 1);
        });
    }

    @Test
    void addProductToCart_shouldThrowException_whenQuantityIsInsufficient() {
        product.setQuantity(5);
        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail("test@example.com")).thenReturn(cart);
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));

        assertThrows(APIException.class, () -> {
            cartService.addProductToCart(101L, 6);
        });
    }

    @Test
    void deleteProductFromCart_shouldRemoveItemAndUpdatePrice() {
        cart.setTotalPrice(200.0);
        CartItem itemToDelete = new CartItem();
        itemToDelete.setProduct(product);
        itemToDelete.setQuantity(2);
        itemToDelete.setProductPrice(100.0);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findCartItemByProductIdAndCartId(101L, 1L)).thenReturn(itemToDelete);

        String result = cartService.deleteProductFromCart(1L, 101L);

        verify(cartItemRepository, times(1)).deleteCartItemByProductIdAndCartId(101L, 1L);
        assertEquals(0.0, cart.getTotalPrice());
        assertTrue(result.contains("removed from the cart"));
    }

    @Test
    void updateProductQuantityInCart_shouldUpdateQuantityAndPrice() {
        int quantityChange = 3;
        cart.setTotalPrice(200.0);

        CartItem existingItem = new CartItem();
        existingItem.setQuantity(2);
        existingItem.setProduct(product);
        existingItem.setProductPrice(100.0);

        when(authUtil.loggedInEmail()).thenReturn("test@example.com");
        when(cartRepository.findCartByEmail("test@example.com")).thenReturn(cart);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findCartItemByProductIdAndCartId(101L, 1L)).thenReturn(existingItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingItem);
        when(modelMapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(cartDTO);

        cartService.updateProductQuantityInCart(101L, quantityChange);

        verify(cartItemRepository, times(1)).save(existingItem);
        assertEquals(5, existingItem.getQuantity());
        assertEquals(500.0, cart.getTotalPrice());
    }

}
