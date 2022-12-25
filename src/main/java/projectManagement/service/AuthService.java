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

    /**
     * register service, register was made from client.
     * check if we have the user id DB, return error if yes.
     * then go to createUser(user, Provider.LOCAL)
     *
     * @param user - UserRequest with email, name, password.
     * @return Response with UserDTO - email, password, name.
     */
    public Response<UserDTO> register(UserRequest user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        try {
            return Response.createSuccessfulResponse(createUser(user, Provider.LOCAL));
        } catch (Exception e) {
            return Response.createFailureResponse("Failed during creation");
        }
    }

    public AuthService() {
    }

    public void save(User user) {
        userRepo.save(user);
    }

    /**
     * login service, check if we have the user id DB,
     * if the provider is local check for password matching.
     *
     * @param user - UserRequest with email, name, password
     * @return Response with UserLoginDTO - userId and token.
     */
    public Response<UserLoginDTO> login(UserRequest user) {
        Optional<User> optUser = userRepo.findByEmail(user.getEmail());
        if (!optUser.isPresent()) {
            return Response.createFailureResponse("user already exist");
        }
        if (optUser.get().getProvider() == Provider.LOCAL) {
            if (!optUser.get().getPassword().equals(user.getPassword())) {
                return Response.createFailureResponse("password do not match");
            }
        }
        return Response.createSuccessfulResponse(new UserLoginDTO(optUser.get().getId(), generateToken(optUser.get().getId())));
    }

    /**
     * called from token filter to check if the id of the user is valid.
     * decode the token to get id from token, then check if exist in DB.
     *
     * @param token -
     * @return - long userId
     * @throws AccountNotFoundException - throw not found an account with this id.
     */
    public Long checkTokenToUserInDB(String token) throws AccountNotFoundException,IllegalAccessError {
        Claims claims = Token.decodeJWT(token);
        if(claims==null) throw new IllegalAccessError("Wrong token input");
        long userId = Long.parseLong(claims.getId());
        Optional<User> user = getUser(userId);
        if (!user.isPresent()) {
            throw new AccountNotFoundException("no id in DB");
        }
        return userId;
    }

    /**
     * getUser from DB
     *
     * @param userId -
     * @return - Optional<User>.
     */
    public Optional<User> getUser(long userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    /**
     * after user authenticate with gitHub, he gets a code as a parameter.
     * we send the parameter from the client to the controller to here.
     * then we need to take 3 actions with this code to get the user email and name.
     *
     * @param code - unique code valid for 5 minutes
     * @return - Response with UserLoginDTO - new userId and token.
     */
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
                    return login(new UserRequest(githubUser.getEmail(), githubUser.getAccessToken()));
                } else {

                }
            }
            return Response.createFailureResponse("user already exist");
        } catch (SQLDataException e) {
            return Response.createFailureResponse("user already exist");
        }
    }

    /**
     * First request to gitHub authorization process
     * we need to get the token authorization from <a href="https://github.com/login/oauth/access_token"></a>
     * with our env.getProperty - github.client-id, github.client-secret.
     *
     * @param code - the parameter after authorization from client.
     * @return
     */
    public ResponseEntity<GitToken> getGithubToken(String code) {
        String baseLink = "https://github.com/login/oauth/access_token?";
        String clientId = env.getProperty("spring.security.oauth2.client.registration.github.client-id");
        String clientSecret = env.getProperty("spring.security.oauth2.client.registration.github.client-secret");
        String linkGetToken = baseLink + "client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
        return GitRequest.reqGitGetToken(linkGetToken);
    }

    /**
     * the second call, we get the user info.
     * from <a href="https://api.github.com/user"></a>
     *
     * @param code - code as a parameter, to get the token.
     * @return GitUser login; name; email; accessToken;
     */
    public GitUser getGithubUser(String code) {
        ResponseEntity<GitToken> gitTokenResponse = getGithubToken(code);
        if (gitTokenResponse != null) {
            try {
                String token = gitTokenResponse.getBody().getAccess_token();
                String linkGetUser = "https://api.github.com/user";
                return GitRequest.reqGitGetUser(linkGetUser, token).getBody();
            } catch (NullPointerException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Create user if email isn't already exist
     *
     * @param user     -
     * @param provider -
     * @return -
     * @throws SQLDataException -
     */
    public UserDTO createUser(UserRequest user, Provider provider) throws SQLDataException {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new SQLDataException(String.format("Email %s already exists!", user.getEmail()));
        }
        //todo: add init notification
        return new UserDTO(userRepo.save(User.CreateUser(user.getName(), user.getEmail(), user.getPassword(), provider)));
    }

    /**
     * generateToken is a function that creates a unique JWT token for every new logged-in user.
     *
     * @param userid - userid
     * @return generated token according to: io.jsonwebtoken.Jwts library
     */
    private String generateToken(long userid) {
        return Token.createJWT(String.valueOf(userid), "Project Management", "login", Instant.now().toEpochMilli());
    }

}
