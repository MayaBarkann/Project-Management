package projectManagement.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import projectManagement.service.AuthService;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(0)
public class TokenFilter extends GenericFilterBean{
    private static Logger logger = LogManager.getLogger(TokenFilter.class.getName());

    @Autowired
    AuthService userService;

    /**
     * this doFilter function is set to check if the user has the permission to enter the app controllers.
     * checks if the request was according to what we need with token in the authorization Header.
     *
     * @param request  - request from client
     * @param response - response if the action can be done or not.
     * @param chain    - chain of filters to go through
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!url.contains("auth") && !url.contains("ws") && !url.contains("error") && !httpRequest.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            if (httpRequest.getHeader("authorization") != null) {
                String token = httpRequest.getHeader("authorization");
                if (token == null) {
                    logger.error("in AuthorizationFilter -> doFilter -> token is null");
                    ((HttpServletResponse) response).setStatus(400);
                    response.getOutputStream().write("token is null".getBytes());
                    return;
                }
                try {
                    Long userId = userService.checkTokenToUserInDB(token.substring(7));
                    request.setAttribute("User", userService.getUser(userId));
                } catch (AccountNotFoundException | IllegalAccessError e) {
                    logger.error("in AuthorizationFilter -> doFilter -> " + e.getMessage());
                    //Servers send 404 instead of 403 Forbidden to hide the existence
                    // of a resource from an unauthorized client.
                    ((HttpServletResponse) response).setStatus(404);
                    response.getOutputStream().write(e.getMessage().getBytes());
                    return;
                }
            } else {
                logger.error("in AuthorizationFilter -> doFilter -> Could not find a token in the request");
                ((HttpServletResponse) response).setStatus(400);
                response.getOutputStream().write("Something in the request wasn't properly written, try again".getBytes());
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
