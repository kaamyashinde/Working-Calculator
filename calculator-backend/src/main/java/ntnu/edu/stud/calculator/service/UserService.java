package ntnu.edu.stud.calculator.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Register a new user
    public User registerUser(String username, String password){
        User user = new User(username, password);
        return userRepository.save(user);
    }

    //Log in an user
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username).filter(user -> user.getPassword().equals(password));
    }

}
