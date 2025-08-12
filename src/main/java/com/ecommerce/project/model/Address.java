package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street must be at least 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building must be at least 5 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 2, message = "City must be at least 2 characters long")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State must be at least 2 characters long")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country must be at least 2 characters long")
    private String country;

    @NotBlank
    @Size(min = 4, message = "Zipcode must be at least 4 characters long")
    private String zipcode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String zipcode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

}
