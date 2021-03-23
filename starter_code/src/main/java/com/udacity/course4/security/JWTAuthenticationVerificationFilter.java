package com.udacity.course4.security;

import com.auth0.jwt.*;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.udacity.course4.common.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {

    //AuthenticationManager authManager;

    public JWTAuthenticationVerificationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken token  = verifyJwtToken(header);

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken verifyJwtToken(String header) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityConstants.SECRET_KEY)).build();

        DecodedJWT decodedJWT = verifier.verify(header.replace(SecurityConstants.TOKEN_PREFIX, ""));

        String username = decodedJWT.getSubject();

        if(username != null) {
            return new UsernamePasswordAuthenticationToken(username, null, null);
        }

        return null;
    }
}
