package gr.aueb.cf.myreserva.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * This class implements Spring Security’s AuthenticationEntryPoint, meaning it defines how to respond when authentication is required but missing.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Set the response status to 401 unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String json = "{\"code\": \"userNotAuthenticated\", \"description\": \"User must authenticate in order to access this endpoint\"}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
