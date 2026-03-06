package com.qard.QardHasanah.config;

import com.qard.QardHasanah.entity.Role;
import com.qard.QardHasanah.entity.User;
import com.qard.QardHasanah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default Super Admin if not exists
        String adminEmail = "admin@qardhasanah.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.SUPER_ADMIN);
            admin.setIsActive(true);
            admin.setIsEmailVerified(true);
            admin.setCreatedAt(System.currentTimeMillis());
            admin.setUpdatedAt(System.currentTimeMillis());

            userRepository.save(admin);
            System.out.println("==============================================");
            System.out.println("Default Super Admin created:");
            System.out.println("Email: " + adminEmail);
            System.out.println("Password: admin123");
            System.out.println("Please change the password after first login!");
            System.out.println("==============================================");
        }
    }
}

