package projectManagement.service;

import javax.security.auth.login.AccountNotFoundException;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectManagement.controller.entities.*;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;
import projectManagement.utils.GitRequest;
import projectManagement.utils.Provider;
import projectManagement.utils.Token;

import java.sql.SQLDataException;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    private Environment env;

    public Response<UserDTO> register(UserRequest user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        try{
            return Response.createSuccessfulResponse(createUser(user,Provider.LOCAL));
        } catch (Exception e) {
            return Response.createFailureResponse("Failed during creation");
        }
    }

    public AuthService() {
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public Response<UserLoginDTO> login(UserRequest user) {
        Optional<User> optUser = userRepo.findByEmail(user.getEmail());
        if (!optUser.isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        if(optUser.get().getProvider()== Provider.LOCAL) {
            if (!optUser.get().getPassword().equals(user.getPassword())) {
                return Response.createFailureResponse("password do not match");
            }
        }
        return Response.createSuccessfulResponse(new UserLoginDTO(optUser.get().getId(),generateToken(optUser.get().getId())));
    }

    /**
     * called from token filter to check if the id of the user is valid.
     * @param token -
     * @return -
     * @throws AccountNotFoundException
     */
    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException {
        Claims claims = Token.decodeJWT(token);
        long userId = Long.parseLong(claims.getId());
        Optional<User> user = getUser(userId);
        if(! user.isPresent()){
            throw new AccountNotFoundException("no id in DB");
        }
        return userId;
    }

    public Optional<User> getUser(long userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }



    public Response<UserLoginDTO> loginGithub(String code) {
        GitUser githubUser = getGithubUser(code);
        try {
            if (githubUser != null) {
                if (githubUser.getEmail() != null) {
                    if (!userRepo.findByEmail(githubUser.getEmail()).isPresent()) {
                        if (githubUser.getName() != "" && githubUser.getName() != null) {
                            UserDTO userCreated = createUser(new UserRequest(githubUser.getEmail(), githubUser.getAccessToken(), githubUser.getName()), Provider.GITHUB);
                        } else {
                            UserDTO userCreated = createUser(new UserRequest(githubUser.getEmail(), githubUser.getLogin(), ""), Provider.GITHUB);
                        }
                    }
                    // if user with email created and has password ??
                    return login(new UserRequest(githubUser.getEmail(), githubUser.getAccessToken()));
                }else{

                }
            }
            return Response.createFailureResponse("user already exist");
        } catch (SQLDataException e) {
            return Response.createFailureResponse("user already exist");
        }
    }

    public ResponseEntity<GitToken> getGithubToken(String code) {
        String baseLink = "https://github.com/login/oauth/access_token?";
        String clientId = env.getProperty("spring.security.oauth2.client.registration.github.client-id");
        String clientSecret = env.getProperty("spring.security.oauth2.client.registration.github.client-secret");
        String linkGetToken = baseLink + "client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
        return GitRequest.reqGitGetToken(linkGetToken);
    }

    public GitUser getGithubUser( String code ) {
        ResponseEntity<GitToken> gitTokenResponse = getGithubToken(code);

        if (gitTokenResponse != null) {
            String token = gitTokenResponse.getBody().getAccess_token();

            String linkGetUser = "https://api.github.com/user";
            return GitRequest.reqGitGetUser(linkGetUser, token).getBody();
        }
        return null;
    }


    /**
     * Create user if email isn't already exist
     * @param user
     * @return the created User
     */
    public UserDTO createUser(UserRequest user, Provider provider) throws SQLDataException {
        if(userRepo.findByEmail(user.getEmail()).isPresent()){
            throw new SQLDataException(String.format("Email %s already exists!", user.getEmail()));
        }
        return new UserDTO(userRepo.save(User.CreateUser(user.getName(), user.getEmail(),user.getPassword(),provider)));
    }

    /**
     * generateToken is a function that creates a unique JWT token for every logged-in user.
     *
     * @param userid - userid
     * @return generated token according to: io.jsonwebtoken.Jwts library
     */
    private String generateToken(long userid) {
        return Token.createJWT(String.valueOf(userid), "Project Management", "login",  Instant.now().toEpochMilli());
    }

}
