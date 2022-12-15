package projectManagement.service;

import javax.security.auth.login.AccountNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    UserRepo userRepo;

    public UserService() {
    }

    public void save(User user) {
        userRepo.save(user);
    }


    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException {
        //FIXME: add real database check, and token decoder
        long id = 2L;
        return id;
    }
}
