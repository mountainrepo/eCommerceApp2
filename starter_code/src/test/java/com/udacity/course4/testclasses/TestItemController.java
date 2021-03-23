package com.udacity.course4.testclasses;

import com.udacity.course4.model.persistence.Item;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import org.junit.jupiter.api.*;

public class TestItemController {

    private String requestPath;
    private String localhostPath;
    private TestRestTemplate restTemplate;
    private String authHeader;

    private String username = "retail_user";
    private Long itemId = 1L;
    private Long wrongItemId = 1000L;
    private String itemName = "Round Widget";
    private String wrongItemName = "Widget";

    public TestItemController(String path, String localhostPath, TestRestTemplate restTemplate, String authToken) {
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

    public void testGetAllItems() {
        String path = requestPath;

        HttpEntity<String> requestEntity = new HttpEntity<>(null, getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, List.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    public void testGetItemById() {
        ResponseEntity<Item> response = getItemByIdRequest(itemId);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().getName(), itemName);
    }

    public void testGetItemById_ItemIdNull() {
        ResponseEntity<Item> response = getItemByIdRequest(null);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    public void testGetItemById_ItemNotFound() {
        ResponseEntity<Item> response = getItemByIdRequest(wrongItemId);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Item> getItemByIdRequest(Long id) {
        String path = requestPath + "/" + id;

        HttpEntity<String> requestEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<Item> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, Item.class);

        return response;
    }

    public void testGetItemByName() {
        ResponseEntity<List> response = getItemByNameRequest(itemName);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        //Assertions.assertEquals(response.getBody().getId(), itemId);
    }

    public void testGetItemByName_ItemNameNull() {
        ResponseEntity<List> response = getItemByNameRequest(null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    public void testGetItemByName_ItemNameNotFound() {
        ResponseEntity<List> response = getItemByNameRequest(wrongItemName);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<List> getItemByNameRequest(String name) {
        String path = requestPath + "/name/" + name;

        HttpEntity<String> requestEntity = new HttpEntity<>(null, getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, List.class);

        return response;
    }
}
