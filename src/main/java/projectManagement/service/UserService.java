package projectManagement.service;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    @Autowired
    UserRepo userRepo;

    public Optional<User> getUser(long userId) {
        logger.info("in UserService -> getUser by id: "+userId);
        return userRepo.findById(userId);
    }
    public Optional<User> getUserByEmail(String email) {
        logger.info("in UserService -> getUserByEmail: "+email);
        return userRepo.findByEmail(email);
    }
}