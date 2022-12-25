package projectManagement.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    UserRepo userRepo;
    public Optional<User> getUser(long userId) {
        return userRepo.findById(userId);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}