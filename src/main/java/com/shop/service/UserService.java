package com.shop.service;

import com.shop.dto.RegisterRequest;
import com.shop.dto.UserDto;
import com.shop.entity.User;
import com.shop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findActiveUserByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User foundUser = user.get();
        return new org.springframework.security.core.userdetails.User(
                foundUser.getEmail(),
                foundUser.getPasswordHash(),
                new ArrayList<>());
    }

    public UserDto registerUser(RegisterRequest registerRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("User with email " + registerRequest.getEmail() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setShopName(registerRequest.getShopName());
        user.setMobile(registerRequest.getMobile());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findActiveUserByEmail(email);
    }

    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findActiveUserByEmail(email);
        return user.map(this::convertToDto).orElse(null);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public UserDto updateUser(String email, UserDto userDto) {
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields if provided
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getShopName() != null) {
            user.setShopName(userDto.getShopName());
        }
        if (userDto.getMobile() != null) {
            user.setMobile(userDto.getMobile());
        }

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public void deactivateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    private UserDto convertToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setShopName(user.getShopName());
        dto.setMobile(user.getMobile());
        // dto.setAddress(user.getAddress()); // Temporarily commented out
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}