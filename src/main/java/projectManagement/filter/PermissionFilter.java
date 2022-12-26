package projectManagement.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import projectManagement.entities.Board;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.filter.entities.MutableHttpServletRequest;
import projectManagement.service.PermissionsService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(3)
public class PermissionFilter implements Filter {
    public static final Logger logger = LogManager.getLogger(PermissionFilter.class);
    PermissionsService permissionsService;
    private static final String[] patterns = {"^/board/(filter|create-board|get-board).*$", "^/auth/.*$"};

    public PermissionFilter(PermissionsService permissionsService){
        this.permissionsService = permissionsService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MutableHttpServletRequest req =new MutableHttpServletRequest ((HttpServletRequest) servletRequest);
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestUrl = httpRequest.getRequestURI();

        if(requestUrl.matches(patterns[0]) || requestUrl.matches(patterns[1])){
            filterChain.doFilter(req,res);
        }else{
            User user = req.getUserAttribute();
            String boardIdStr = servletRequest.getParameter("boardId");
            if (user != null || boardIdStr != null) {
                try{
                    long boardId = Long.parseLong(boardIdStr);
                    String methodName = getMethodNameFromUrl(servletRequest);
                    System.out.println("In permission filter -> method : " + methodName);
                    Response<Board> hasPermissionResponse = permissionsService.checkPermission(user.getId(), boardId, methodName);
                    if(hasPermissionResponse.isSucceed()) {
                        req.setAttribute("board", hasPermissionResponse.getData());
                        filterChain.doFilter(req,res);
                    } else {
                        servletResponse.getOutputStream().write(hasPermissionResponse.getMessage().getBytes());
                        return;
                    }
                }catch (NumberFormatException e){
                    servletResponse.getOutputStream().write(e.getMessage().getBytes());
                    return;
                }

            }

            servletResponse.getOutputStream().write("board not found".getBytes());
        }
    }

//    /**
//     * Sends an error response to the client using status code 401, with message Unauthorized.
//     * @param res, HttpServletResponse object, contains response to a servlet request.
//     * @throws IOException, if an input or output exception occurs.
//     */
//    private void returnBadResponse(HttpServletResponse res) throws IOException {
//        res.sendError(401, "Unauthorized");
//    }

    private String getMethodNameFromUrl(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUrl = httpRequest.getRequestURL().toString();
        return requestUrl.substring(requestUrl.lastIndexOf('/') + 1);

    }
}
