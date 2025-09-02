package com.shop.config;

import com.shop.entity.User;
import com.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

// @Component - Temporarily disabled to fix schema issues
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if it doesn't exist
        if (!userRepository.existsByEmail("admin@shop.com")) {
            User adminUser = new User();
            adminUser.setId("default-admin-user");
            adminUser.setName("Default Admin");
            adminUser.setEmail("admin@shop.com");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            adminUser.setShopName("Default Shop");
            adminUser.setMobile("0000000000");
            adminUser.setIsActive(true);

            userRepository.save(adminUser);
            System.out.println("✅ Default admin user created successfully!");
            System.out.println("📧 Email: admin@shop.com");
            System.out.println("🔑 Password: admin123");
        } else {
            System.out.println("ℹ️ Default admin user already exists");
        }
    }
}