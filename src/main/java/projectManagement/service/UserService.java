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

    /**
     * Gets a user with a given ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return An optional containing the user with the given ID, if it exists.
     */
    public Optional<User> getUser(long userId) {
        logger.info("in UserService -> getUser by id: " + userId);
        return userRepo.findById(userId);
    }

    /**
     * Gets a user with a given email address.
     *
     * @param email The email address of the user to retrieve.
     * @return An optional containing the user with the given email address, if it exists.
     */
    public Optional<User> getUserByEmail(String email) {
        logger.info("in UserService -> getUserByEmail: " + email);
        return userRepo.findByEmail(email);
    }
}