package com.udacity.course4.testclasses;

import com.udacity.course4.model.persistence.UserOrder;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

public class TestOrderController {

    private String requestPath;
    private String localhostPath;
    private TestRestTemplate restTemplate;
    private String authHeader;

    private String username = "retail_user";
    private String wrongUsername = "retail";
    private Long itemId = 1L;
    private Long wrongItemId = 1000L;
    private String itemName = "Round Widget";
    private String wrongItemName = "Widget";
    private int quantity = 2;

    public TestOrderController(String path, String localhostPath, TestRestTemplate restTemplate, String authToken) {
        this.requestPath = path;
        this.localhostPath = localhostPath;
        this.restTemplate = restTemplate;
        this.authHeader = authToken;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, authHeader);

        return headers;
    }

    public void testSubmit() {
        ResponseEntity<UserOrder> response = submitRequest(username);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    public void testSubmit_UsernameNull() {
        ResponseEntity<UserOrder> response = submitRequest(null);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testSubmit_UsernameNotFound() {
        ResponseEntity<UserOrder> response = submitRequest(wrongUsername);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<UserOrder> submitRequest(String name) {
        String path = requestPath + "/submit/" + name;

        HttpEntity<String> requestEntity = new HttpEntity<>(name, getHeaders());
        ResponseEntity<UserOrder> responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestEntity, UserOrder.class);

        return responseEntity;
    }

    ///////

    public void testHistory() {
        ResponseEntity<List> response = historyRequest(username);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    public void testHistory_UsernameNull() {
        ResponseEntity<List> response = historyRequest(null);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testHistory_UsernameNotFound() {
        ResponseEntity<List> response = historyRequest(wrongUsername);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<List> historyRequest(String name) {
        String path = requestPath + "/history/" + name;

        HttpEntity<String> requestEntity = new HttpEntity<>(name, getHeaders());
        ResponseEntity<List> responseEntity = restTemplate.exchange(path, HttpMethod.GET, requestEntity, List.class);

        return responseEntity;
    }
}
