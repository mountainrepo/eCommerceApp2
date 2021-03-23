package com.udacity.course4;

import com.udacity.course4.testclasses.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ECommerceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String localhost = "http://localhost:";
	private String authToken = null;

	@Test
	public void testController() {
		// Test User controller
		testUserController();

		// Test item controller
		testItemController();

		// Test cart controller
		testCartController();

		// Test order controller
		testOrderController();
	}

	private void testUserController() {
		String path = "/api/user";

		TestUserController testUserController = new TestUserController(localhost + port + path, localhost + port, restTemplate);

		testUserController.testCreateUser_Success();
		testUserController.testCreateUser_PasswordInvalid();
		testUserController.testCreateUser_ConfirmPasswordInvalid();

		testUserController.testLogin();

		testUserController.testFindById();
		testUserController.testFindById_IdNotFound();

		testUserController.testFindByUsername();
		testUserController.testFindByUsername_UsernameNotFound();

		authToken = testUserController.getAuthorizationToken();
	}

	private void testItemController() {
		String path = "/api/item";

		TestItemController testItemController = new TestItemController(localhost + port + path, localhost + port, restTemplate, authToken);

		testItemController.testGetAllItems();

		testItemController.testGetItemById();
		testItemController.testGetItemById_ItemIdNull();
		testItemController.testGetItemById_ItemNotFound();

		testItemController.testGetItemByName();
		//testItemController.testGetItemByName_ItemNameNull();
		testItemController.testGetItemByName_ItemNameNotFound();
	}

	private void testCartController() {
		String path = "/api/cart";

		TestCartController testItemController = new TestCartController(localhost + port + path, localhost + port, restTemplate, authToken);

		testItemController.testAddToCart();
		testItemController.testAddToCart_UsernameNull();
		testItemController.testAddToCart_UsernameNotFound();
		testItemController.testAddToCart_ItemIdNotFound();

		testItemController.testRemoveFromCart();
		testItemController.testRemoveFromCart_UsernameNull();
		testItemController.testRemoveFromCart_UsernameNotFound();
		testItemController.testRemoveFromCart_ItemIdNotFound();
	}

	private void testOrderController() {
		String path = "/api/order";

		TestOrderController testOrderController = new TestOrderController(localhost + port + path, localhost + port, restTemplate, authToken);

		testOrderController.testSubmit();
		//testOrderController.testSubmit_UsernameNull();
		testOrderController.testSubmit_UsernameNotFound();

		testOrderController.testHistory();
		//testOrderController.testHistory_UsernameNull();
		testOrderController.testHistory_UsernameNotFound();
	}
}
