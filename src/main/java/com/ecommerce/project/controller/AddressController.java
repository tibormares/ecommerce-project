package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Address", description = "APIs for managing user's addresses")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    AuthUtil authUtil;

    @Operation(summary = "Create an address for the current user", description = "Adds a new address to the authenticated user's profile. Requires user authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data provided"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);

        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all addresses (Admin)", description = "Retrieves a list of all addresses in the system. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all addresses"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - User is not authenticated or not an admin")
    })
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddresses();

        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Get an address by ID (Admin)", description = "Retrieves a specific address by its ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the address"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - User is not authenticated or not an admin"),
            @ApiResponse(responseCode = "404", description = "Address with the given ID not found")
    })
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get addresses for the current user", description = "Retrieves a list of all addresses associated with the authenticated user. Requires user authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's addresses"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOList = addressService.getUserAddresses(user);

        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Update an address", description = "Updates a specific address by its ID. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - User is not authenticated or not authorized to update this address"),
            @ApiResponse(responseCode = "404", description = "Address with the given ID not found")
    })
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddress(addressId, addressDTO);

        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete an address", description = "Deletes a specific address by its ID. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - User is not authenticated or not authorized to delete this address"),
            @ApiResponse(responseCode = "404", description = "Address with the given ID not found")
    })
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
