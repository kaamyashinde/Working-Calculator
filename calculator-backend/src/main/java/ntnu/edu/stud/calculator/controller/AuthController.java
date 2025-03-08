package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    //Endpoint for registering a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            logger.info("Registering new user: {}", user.getUsername());
            User newUser = userService.registerUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    //Endpoint for loggin in an user, without sessions / tokens (will be implemented in part 2)
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        return userService.login(user.getUsername(), user.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }
}
