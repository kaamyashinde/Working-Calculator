package ntnu.edu.stud.calculator.service;

import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User register(String username, String password) {
        User user = new User(username, password);
        return userRepository.save(user);
    }

    // Authenticate user by username and password
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }
}
