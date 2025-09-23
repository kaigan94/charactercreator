package com.rpg.charactercreator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filter som körs en gång per request.
 * Hämtar CSRF-token från requesten så att den kan skickas som cookie till frontend.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Plocka upp CSRF-token från request-attributen
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            token.getToken(); // Triggar att token skrivs till cookien
        }

        // Skicka vidare request/response i filterkedjan
        filterChain.doFilter(request, response);
    }
}