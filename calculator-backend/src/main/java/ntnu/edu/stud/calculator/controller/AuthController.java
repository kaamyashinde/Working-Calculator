package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.security.JwtUtil;
import ntnu.edu.stud.calculator.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Authentication Controller", description = "Handles user authentication and registration")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user and generates a JWT token")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            logger.info("Registering new user: {}", user.getUsername());
            User newUser = userService.registerUser(user.getUsername(), user.getPassword());
            String token = jwUtil.generateToken(newUser.getUsername());
            LoginResponse response = new LoginResponse(token, newUser);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Login a user", description = "Generates a JWT token for the user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword())
                .map(user -> {
                    String token = jwUtil.generateToken(user.getUsername());
                    LoginResponse response = new LoginResponse(token, user);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(new LoginResponse(null, null)));
    }

    @Operation(summary = "Refresh a token", description = "Generates a new JWT token for the user")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String username = jwUtil.validateTokenAndGetUsername(token);
                    
            if (username != null) {
                String newToken = jwUtil.generateToken(username);
                return ResponseEntity.ok(new LoginResponse(newToken, null));
            }
        }
        return ResponseEntity.status(401).body(new LoginResponse(null, null));
    }
}

class LoginResponse {
    private String token;
    private User user;
    private String error;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.error = token == null ? "Invalid credentials" : null;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getError() {
        return error;
    }
}