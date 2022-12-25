package projectManagement.service;

import javax.security.auth.login.AccountNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;
import projectManagement.utils.Validation;

import java.util.Optional;

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
}
