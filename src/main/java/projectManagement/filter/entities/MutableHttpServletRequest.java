package projectManagement.filter.entities;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders;
    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        customHeaders=new HashMap<>();
    }

    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null){
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }

}
