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


    public Response<User> register(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        return Response.createSuccessfulResponse(userRepo.save(new User(user.getName(), user.getEmail(), user.getPassword())));
    }

    public UserService() {
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public Response<String> login(User user) {
        Optional<User> optUser = userRepo.findByEmail(user.getEmail());
        if (!optUser.isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        if (!optUser.get().getPassword().equals(user.getPassword())) {
            return Response.createFailureResponse("user already exist");
        }
        // TODO: CREATE generateToken
        return Response.createSuccessfulResponse("token");
    }

    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException {
        //FIXME: add real database check, and token decoder
        long id = 2L;
        return id;
    }

    public Optional<User> getUser(long userId) {
        return userRepo.findById(userId);
    }
}
