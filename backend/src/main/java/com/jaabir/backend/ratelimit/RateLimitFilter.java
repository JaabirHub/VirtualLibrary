package com.jaabir.backend.ratelimit;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jaabir.backend.user.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Search endpoint: user-based (or IP fallback)
        if (isSearchRequest(request)) {
            String key = getClientKey(request);

            if (!rateLimitService.allowSearchRequest(key)) {
                respondWith429(response, 60);
                return;
            }
        }

        // Login endpoint: IP-based
        if (isLoginRequest(request)) {
            String key = "ratelimit:login:ip:" + getClientIp(request);

            if (!rateLimitService.allowLoginRequest(key)) {
                respondWith429(response, 60);
                return;
            }
        }

        // Register endpoint: IP-based
        if (isRegisterRequest(request)) {
            String key = "ratelimit:register:ip:" + getClientIp(request);

            if (!rateLimitService.allowRegisterRequest(key)) {
                respondWith429(response, 60);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSearchRequest(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod())
                && "/api/books/search".equals(request.getRequestURI());
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && "/api/auth/login".equals(request.getRequestURI());
    }

    private boolean isRegisterRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && "/api/auth/register".equals(request.getRequestURI());
    }

    private String getClientKey(HttpServletRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User user) {

            return "ratelimit:search:user:" + user.getId();
        }

        return "ratelimit:search:ip:" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void respondWith429(HttpServletResponse response, long retryAfterSeconds)
            throws IOException {
        response.setStatus(429);
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"error\":\"Too many requests\",\"retryAfterSeconds\":" + retryAfterSeconds + "}"
        );
    }
}