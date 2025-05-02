package com.vdt.vdt.security;

import com.vdt.vdt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Define paths to skip from authorization
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login","/**",
            "/api/auth/reset-password",
            "/swagger-ui/**",
            "/api/auth/pre-login-token",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/index.html"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip authorization for excluded paths
        if (isExcludedPath(path)) {
            System.out.println("excluded paths...........");
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing or invalid Authorization header.");
            return;
        }

        String token = authorizationHeader.substring(7); // remove "Bearer "

        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or expired token.");
            return;
        }

        // Token is valid, continue to controller
        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        /*System.out.println(path+"\n\n................\n"+EXCLUDED_PATHS);
        return EXCLUDED_PATHS.stream().anyMatch(path::equals);*/
        return true;
    }
}
