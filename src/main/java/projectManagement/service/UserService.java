package projectManagement.service;

import javax.security.auth.login.AccountNotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class UserService {




    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException {
        //FIXME: add real database check, and token decoder
        long id =2L;
        return id;
    }
}
