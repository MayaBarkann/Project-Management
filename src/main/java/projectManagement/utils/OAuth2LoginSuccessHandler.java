package projectManagement.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import projectManagement.entities.CustomAuth2User;
import projectManagement.entities.User;
import projectManagement.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomAuth2User user = (CustomAuth2User) authentication.getPrincipal();
        String email = user.getLogin();
        String displayName = user.getFullName();
        System.out.println("login Name:" + email);
        Optional<User> optionalUser = userService.getUserByEmail(email);
        if(! optionalUser.isPresent()){
            userService.registerAfterAuthLoginSuccess(email,displayName,Provider.GITHUB);
            System.out.println("new User login Github");
        }else{
            userService.updateExistingCustomer(optionalUser.get(),displayName,Provider.GITHUB);
            System.out.println("Existing User Github");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
