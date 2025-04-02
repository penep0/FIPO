package springproject.financeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.User;
import springproject.financeproject.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get a user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user details
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNickName(userDetails.getNickName());
            user.setPassword(userDetails.getPassword());
            user.setMoney(userDetails.getMoney());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void addPortfolio(User user, Portfolio portfolio) {
        List<Portfolio> userPortfolio = user.getPortfolios();
        userPortfolio.add(portfolio);
        user.setPortfolios(userPortfolio);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
