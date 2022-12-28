package projectManagement.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.filter.entities.MutableHttpServletRequest;
import projectManagement.service.AuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class TokenFilter implements Filter {
    public static final Logger logger = LogManager.getLogger(TokenFilter.class);
    private final AuthService authService;
    private static final String pattern = "(^/auth/.*$)|(^/ws/.*$)";

    public TokenFilter(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Processes a request/response pair passed through the Filter Chain due to a client request for a resource at the end of the chain.
     * The token in the header of the request is being checked, if token is valid and correct, this filter passes on the request and response to the next entity in the chain.
     * If token invalid the filter return an Unauthorized response.
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain     Provides access to the next filter in the chain for this
     *                        filter to pass the request and response to for further
     *                        processing
     * @throws IOException      if an I/O exception occurs during the processing of the request/response.
     * @throws ServletException if the processing fails.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestUrl = httpRequest.getRequestURI();
        MutableHttpServletRequest req = new MutableHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if (requestUrl.matches(pattern)) {
            filterChain.doFilter(req, res);
        } else {
            System.out.println(req.getRequestURL().toString());
            String authToken = req.getHeader("Authorization");
            if (authToken != null && !authToken.isEmpty()) {
                Response<User> tokenCorrect = authService.isTokenCorrect(authToken);
                if (tokenCorrect.isSucceed()) {
                    req.setAttribute("user", tokenCorrect.getData());
                    logger.info("passing request");
                    filterChain.doFilter(req, res);
                } else {
                    returnBadResponse(res, "Invalid token");
                }
            } else {
                returnBadResponse(res, "Token can not be null or empty");
            }

        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * Sends an error response to the client using status code 401, with message Unauthorized.
     * @param res, HttpServletResponse object, contains response to a servlet request.
     * @throws IOException, if an input or output exception occurs.
     */
    private void returnBadResponse(HttpServletResponse res, String message) throws IOException {
        res.sendError(401, message);
    }


}
