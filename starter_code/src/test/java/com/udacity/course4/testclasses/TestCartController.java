package com.udacity.course4.testclasses;

import com.udacity.course4.model.persistence.Cart;
import com.udacity.course4.model.requests.ModifyCartRequest;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

public class TestCartController {

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

    public TestCartController(String path, String localhostPath, TestRestTemplate restTemplate, String authToken) {
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

    public void testAddToCart() {
        ResponseEntity<Cart> responseEntity = addToCartRequest(username, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    public void testAddToCart_UsernameNull() {
        ResponseEntity<Cart> responseEntity = addToCartRequest(null, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testAddToCart_UsernameNotFound() {
        ResponseEntity<Cart> responseEntity = addToCartRequest(wrongUsername, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    public void testAddToCart_ItemIdNotFound() {
        ResponseEntity<Cart> responseEntity = addToCartRequest(username, wrongItemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Cart> addToCartRequest(String username, long cartItemId, int cartQuantity) {
        String path = requestPath + "/addToCart";

        ModifyCartRequest cartRequest = new ModifyCartRequest(username, cartItemId, cartQuantity);
        HttpEntity<ModifyCartRequest> requestEntity = new HttpEntity<>(cartRequest, getHeaders());

        ResponseEntity<Cart> responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestEntity, Cart.class);

        return responseEntity;
    }

    ///////

    public void testRemoveFromCart() {
        ResponseEntity<Cart> responseEntity = RemoveFromCartRequest(username, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    public void testRemoveFromCart_UsernameNull() {
        ResponseEntity<Cart> responseEntity = RemoveFromCartRequest(null, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testRemoveFromCart_UsernameNotFound() {
        ResponseEntity<Cart> responseEntity = RemoveFromCartRequest(wrongUsername, itemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    public void testRemoveFromCart_ItemIdNotFound() {
        ResponseEntity<Cart> responseEntity = RemoveFromCartRequest(username, wrongItemId, quantity);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Cart> RemoveFromCartRequest(String username, long cartItemId, int cartQuantity) {
        String path = requestPath + "/removeFromCart";

        ModifyCartRequest cartRequest = new ModifyCartRequest(username, cartItemId, cartQuantity);
        HttpEntity<ModifyCartRequest> requestEntity = new HttpEntity<>(cartRequest, getHeaders());

        ResponseEntity<Cart> responseEntity = restTemplate.exchange(path, HttpMethod.POST, requestEntity, Cart.class);

        return responseEntity;
    }
}
