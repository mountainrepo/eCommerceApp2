package com.udacity.course4.testclasses;

import com.udacity.course4.controllers.UserController;
import com.udacity.course4.helper.LoginRequest;
import com.udacity.course4.model.persistence.User;
import com.udacity.course4.model.requests.CreateUserRequest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import org.junit.jupiter.api.*;

public class TestUserController {

    private String requestPath;
    private String localhostPath;
    private TestRestTemplate restTemplate;

    private String username = "retail_user";
    private String password = "user1234";
    private String confirmPassword = "user1234";
    private String invalidPassword = "user123";
    private String wrongConfirmPassword = "u1234567";
    private String wrongUsername = "retail";
    private String authHeader = null;
    private long id = 0;
    private long wrongId = 1000;

    public TestUserController(String path, String localhostPath, TestRestTemplate restTemplate) {
        this.requestPath = path;
        this.localhostPath = localhostPath;
        this.restTemplate = restTemplate;
    }

    public String getAuthorizationToken() {
        return authHeader;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, authHeader);

        return headers;
    }

    public void testCreateUser_Success() {
        ResponseEntity<User> response = createUserRequest(password, confirmPassword);

        User user = response.getBody();
        id = user.getId();

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(user.getUsername(), username);
    }

    private ResponseEntity<User> createUserRequest(String pass, String confirmPass) {
        String path = requestPath + "/create";

        CreateUserRequest userRequest = new CreateUserRequest(username, pass, confirmPass);
        HttpEntity<CreateUserRequest> requestEntity = new HttpEntity<>(userRequest);

        ResponseEntity<User> response = restTemplate.exchange(path, HttpMethod.POST, requestEntity, User.class);

        return response;
    }

    public void testCreateUser_PasswordInvalid() {
        ResponseEntity<User> response = createUserRequest(invalidPassword, confirmPassword);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testCreateUser_ConfirmPasswordInvalid() {
        ResponseEntity<User> response = createUserRequest(password, wrongConfirmPassword);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testLogin() {
        String path = localhostPath + "/login";

        LoginRequest loginRequest = new LoginRequest(username, password);
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest);
        ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.POST, requestEntity, String.class);

        HttpHeaders headers = response.getHeaders();
        authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        // Verify
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    public void testFindById() {
        ResponseEntity<User> response = findByIdRequest(id);

        // Verify
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getUsername(), username);
    }

    private ResponseEntity<User> findByIdRequest(long userId) {
        String path = requestPath + "/id/" + userId;

        HttpEntity<String> requestEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<User> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, User.class);

        return response;
    }

    public void testFindById_IdNotFound() {
        ResponseEntity<User> response = findByIdRequest(wrongId);

        // Verify
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    public void testFindByUsername() {
        ResponseEntity<User> response = findByUsernameRequest(username);

        // Verify
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getId(), id);
    }

    private ResponseEntity<User> findByUsernameRequest(String name) {
        String path = requestPath + "/" + name;

        HttpEntity<String> requestEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<User> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, User.class);

        return response;
    }

    public void testFindByUsername_UsernameNotFound() {
        ResponseEntity<User> response = findByUsernameRequest(wrongUsername);

        // Verify
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
