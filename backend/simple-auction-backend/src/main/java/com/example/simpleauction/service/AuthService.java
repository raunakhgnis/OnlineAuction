package com.example.simpleauction.service;

import com.example.simpleauction.dto.AuthRequest;
import com.example.simpleauction.dto.AuthResponse;
import com.example.simpleauction.dto.MessageResponse;
import com.example.simpleauction.entity.User;
import com.example.simpleauction.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
// Note: No PasswordEncoder here for simplicity, add for real app
// import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final UserRepository userRepository;
    // VERY basic in-memory token store. NOT SECURE.
    // Should use JWTs and potentially a blacklist/refresh mechanism in production.
    final Map<String, String> activeTokens = new ConcurrentHashMap<>();

    // Inject PasswordEncoder if using hashing
    // private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository /*, PasswordEncoder passwordEncoder */) {
        this.userRepository = userRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> registerUser(AuthRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        // HASH THE PASSWORD in real app: passwordEncoder.encode(signUpRequest.getPassword())
        User user = new User(signUpRequest.getEmail(), signUpRequest.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> loginUser(AuthRequest loginRequest) {
        Optional<User> userData = userRepository.findByEmail(loginRequest.getEmail());
        if (userData.isPresent()) {
            User user = userData.get();
            // COMPARE HASHED PASSWORDS in real app: passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())
            if (user.getPassword().equals(loginRequest.getPassword())) {
                String token = UUID.randomUUID().toString();
                activeTokens.put(token, user.getEmail());
                return ResponseEntity.ok(new AuthResponse("Login successful", token, user.getEmail()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Invalid credentials"));
    }

    // Helper to get email from the simple token map
    public String getUserEmailFromToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ") && bearerToken.length() > 7) {
            String token = bearerToken.substring(7);
            return activeTokens.get(token); // Returns email or null if token not found/valid
        }
        return null;
    }

     // Simple check for validity (token exists in map)
    public boolean isValidToken(String bearerToken) {
         return getUserEmailFromToken(bearerToken) != null;
    }

    public void logoutUser(String bearerToken) {
         if (bearerToken != null && bearerToken.startsWith("Bearer ") && bearerToken.length() > 7) {
            String token = bearerToken.substring(7);
            activeTokens.remove(token);
        }
    }
}

