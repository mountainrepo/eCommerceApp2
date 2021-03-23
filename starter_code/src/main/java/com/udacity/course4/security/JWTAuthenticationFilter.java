package com.udacity.course4.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.course4.common.SecurityConstants;
import com.udacity.course4.model.persistence.User;

import com.auth0.jwt.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    AuthenticationManager authManager;
    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Get user
        User user = null;

        System.out.println("Attempt Authentication");

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>());

        return authManager.authenticate(authToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        // Get username
        String username = ((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername();

        // Create header claim
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("alg", SecurityConstants.HMAC256);
        headerMap.put("typ", "JWT");

        // Create JWT token
        String token = JWT.create()
                        .withHeader(headerMap)
                        .withSubject(username)
                        .sign(Algorithm.HMAC256(SecurityConstants.SECRET_KEY));

        // Add JWT token to response header
        response.addHeader("Authorization", SecurityConstants.TOKEN_PREFIX + token);
    }
}
