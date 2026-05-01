package com.berkayozer.user.service.application.security;


import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IdentityProviderPort identityProviderPort;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(IdentityProviderPort identityProviderPort, TokenRepository tokenRepository) {
        this.identityProviderPort = identityProviderPort;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Check if there are tokens in the request.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Is the token valid (has it expired, is the signature correct)?
            if (identityProviderPort.validateToken(token)) {
                String email = identityProviderPort.getEmailFromToken(token);
                Date issueDate = identityProviderPort.getIssueDateFromToken(token);

                // Is token black listed
                if (!tokenRepository.isTokenRevokedForUser(email, issueDate)) {
                    String role = identityProviderPort.getRoleFromToken(token);

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));

                    // We place the user in the Spring Security Context
                    UserDetails userDetails = User.withUsername(email).password("").authorities(authorities).build();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        }

        // Continue the filter chain (Spring automatically throws a 403/401 if no user is set in the Context)
        filterChain.doFilter(request, response);
    }

}