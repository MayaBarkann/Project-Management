package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public UserService() {
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
