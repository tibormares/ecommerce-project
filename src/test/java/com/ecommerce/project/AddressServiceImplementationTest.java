package com.ecommerce.project;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.AddressServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplementationTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressServiceImplementation addressService;

    private User user;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAddresses(new ArrayList<>());

        addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street 123");
        addressDTO.setCity("Test City");
        addressDTO.setCountry("TestCountry");

        address = new Address();
        address.setAddressId(101L);
        address.setStreet("Test Street 123");
        address.setCity("Test City");
        address.setCountry("TestCountry");
    }

    @Test
    void createAddress_shouldCreateAndLinkAddressToUser() {
        when(modelMapper.map(addressDTO, Address.class)).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(modelMapper.map(address, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO result = addressService.createAddress(addressDTO, user);

        assertNotNull(result);
        assertEquals("Test City", result.getCity());
        assertEquals(1, user.getAddresses().size());
        assertEquals("Test Street 123", user.getAddresses().getFirst().getStreet());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void getAddressById_shouldReturnAddress_whenFound() {
        when(addressRepository.findById(101L)).thenReturn(Optional.of(address));
        when(modelMapper.map(address, AddressDTO.class)).thenReturn(addressDTO);

        AddressDTO result = addressService.getAddressById(101L);

        assertNotNull(result);
        assertEquals(addressDTO.getStreet(), result.getStreet());
    }

    @Test
    void getAddressById_shouldThrowException_whenNotFound() {
        when(addressRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.getAddressById(999L);
        });
    }

    @Test
    void updateAddress_shouldUpdateAddress_whenUserIsOwner() {
        address.setUser(user);
        user.getAddresses().add(address);

        AddressDTO updatedDetails = new AddressDTO();
        updatedDetails.setStreet("New Street 456");
        updatedDetails.setCity("New City");
        updatedDetails.setState("New State");
        updatedDetails.setCountry("New Country");
        updatedDetails.setBuildingName("New Building");
        updatedDetails.setZipcode("12345");

        when(addressRepository.findById(101L)).thenReturn(Optional.of(address));

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        addressService.updateAddress(101L, updatedDetails, user);

        verify(addressRepository, times(1)).save(any(Address.class));
        verify(userRepository, times(1)).save(user);

        assertEquals("New Street 456", address.getStreet());
        assertEquals("New City", address.getCity());
    }

    @Test
    void updateAddress_shouldThrowException_whenUserIsNotOwner() {
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        address.setUser(user);

        when(addressRepository.findById(101L)).thenReturn(Optional.of(address));

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.updateAddress(101L, addressDTO, anotherUser);
        });

        verify(addressRepository, never()).save(any());
    }

    @Test
    void deleteAddress_shouldDeleteAddress_whenUserIsOwner() {
        address.setUser(user);

        when(addressRepository.findById(101L)).thenReturn(Optional.of(address));

        String result = addressService.deleteAddress(101L, user);

        assertEquals("Address with addressId: 101 deleted", result);
        verify(addressRepository, times(1)).delete(address);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteAddress_shouldThrowException_whenUserIsNotOwner() {
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        address.setUser(user);

        when(addressRepository.findById(101L)).thenReturn(Optional.of(address));

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.deleteAddress(101L, anotherUser);
        });

        verify(addressRepository, never()).delete(any());
        verify(userRepository, never()).save(any());
    }
}
