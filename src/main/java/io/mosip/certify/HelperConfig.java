package io.mosip.certify;

import io.mosip.certify.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class HelperConfig extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                tokenService.getClaimsFromToken(authorizationHeader);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("Authorization Failed: ", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
