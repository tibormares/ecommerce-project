package com.ecommerce.project.config;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${default.user.password}")
    private String userPassword;

    @Value("${default.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // Retrieve or create roles
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> {
                    Role newUserRole = new Role(AppRole.ROLE_USER);
                    return roleRepository.save(newUserRole);
                });

        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                    return roleRepository.save(newAdminRole);
                });

        Set<Role> userRoles = Set.of(userRole);
        Set<Role> adminRoles = Set.of(userRole, adminRole);

        // Create users if not exist
        if (!userRepository.existsByUserName("user1")) {
            User user1 = new User("user1", "user1@example.com", passwordEncoder.encode(userPassword));
            userRepository.save(user1);
        }

        if (!userRepository.existsByUserName("admin1")) {
            User admin1 = new User("admin1", "admin1@example.com", passwordEncoder.encode(adminPassword));
            userRepository.save(admin1);
        }

        // Update roles for existing users
        userRepository.findByUserName("user1").ifPresent(user -> {
            user.setRoles(userRoles);
            userRepository.save(user);
        });

        userRepository.findByUserName("admin1").ifPresent(admin -> {
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        });
    }
}
