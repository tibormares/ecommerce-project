package com.ecommerce.project;

import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.AddressService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AddressServiceIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;

    @BeforeEach
    void setUp() {
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

        User userToSave = new User();
        userToSave.setEmail("addressuser@example.com");
        userToSave.setUserName("test");
        userToSave.setPassword("password");
        userToSave.setRoles(Set.of(userRole));
        userToSave.setAddresses(new ArrayList<>());

        this.user = userRepository.save(userToSave);
    }

    @Test
    void testCreateAddress_shouldCreateAndLinkAddress() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Integration Street 42");
        addressDTO.setCity("TestCity");
        addressDTO.setCountry("TestCountry");
        addressDTO.setZipcode("12345");
        addressDTO.setState("TestState");
        addressDTO.setBuildingName("TestBuildingName");

        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);

        assertNotNull(savedAddressDTO.getAddressId());
        assertEquals(1, addressRepository.count());

        Address foundAddress = addressRepository.findAll().getFirst();

        assertEquals("Integration Street 42", foundAddress.getStreet());
        assertEquals(user.getUserId(), foundAddress.getUser().getUserId());
    }
}
