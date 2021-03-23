package com.udacity.course4.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.udacity.course4.common.ErrorCode;
import com.udacity.course4.common.Logging;
import com.udacity.course4.common.MessageCode;
import com.udacity.course4.model.persistence.Cart;
import com.udacity.course4.model.persistence.repositories.CartRepository;
import com.udacity.course4.model.persistence.repositories.ItemRepository;
import com.udacity.course4.model.persistence.repositories.UserRepository;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.course4.model.persistence.Item;
import com.udacity.course4.model.persistence.User;
import com.udacity.course4.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	private static Logger logger = LoggerFactory.getLogger(CartController.class);

	private HttpStatus statusNotFound = HttpStatus.NOT_FOUND;
	private HttpStatus statusServerError = HttpStatus.INTERNAL_SERVER_ERROR;
	private HttpStatus statusOk = HttpStatus.OK;
	private HttpStatus statusBadRequest = HttpStatus.BAD_REQUEST;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/cart/addToCart", "addToCart", "entry", null, null, null);

		// Validation
		if(request.getUsername() == null || request.getItemId() == 0 || request.getQuantity() == 0) {
			logInvalidRequest(request);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		// Initialize user
		User user = null;

		// Find user by name
		try {
			user = userRepository.findByUsername(request.getUsername());
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/cart/addToCart", "addToCart", "processing", error.getCode(), error.getDescription(), "User Repository",
					"findByUsername", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify user
		if(user == null) {
			ErrorCode error = ErrorCode.USER_NAME_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/cart/addToCart", "addToCart", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Find item by id
		Optional<Item> item = null;
		try {
			item = itemRepository.findById(request.getItemId());
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/cart/addToCart", "addToCart", "processing", error.getCode(), error.getDescription(), "Item Repository",
					"findById", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify found item
		if(!item.isPresent()) {
			ErrorCode error = ErrorCode.ITEM_ID_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/cart/addToCart", "addToCart", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Add item to cart
		Cart cart = user.getCart();
		final Item cartItem = item.get();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(cartItem));

		// Save cart
		try {
			cartRepository.save(cart);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/cart/addToCart", "addToCart", "processing", error.getCode(), error.getDescription(), "Cart Repository",
					"save", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Log successfully added item to cart
		MessageCode message = MessageCode.ITEM_ADDITION_SUCCESS;
		Logging.logSuccessMessage(logger, "/api/cart/addToCart", "addToCart", "post processing", message.getCode(), message.getDescription());

		// Log exit
		Logging.logMethodExit(logger, "/api/cart/addToCart", "addToCart", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return cart
		logger.info("API method exit - addToCart. Successfully added item to cart. HttpStatus code is " + HttpStatus.OK);
		return new ResponseEntity(cart, HttpStatus.OK);
	}

	private void logInvalidRequest(ModifyCartRequest request) {
		if(request.getUsername() == null) {
			ErrorCode error = ErrorCode.USER_NAME_NULL;
			Logging.logValidationOrProcessingError(logger, "/api/cart/addToCart", "addToCart", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());
		}
		else if(request.getItemId() == 0) {
			ErrorCode error = ErrorCode.ITEM_ID_ZERO;
			Logging.logValidationOrProcessingError(logger, "/api/cart/addToCart", "addToCart", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());
		}
		else {
			ErrorCode error = ErrorCode.QUANTITY_ZERO;
			Logging.logValidationOrProcessingError(logger, "/api/cart/addToCart", "addToCart", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());
		}
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/order/removeFromCart", "removeFromcart", "entry", null, null, null);

		// Validation
		if(request.getUsername() == null || request.getItemId() == 0 || request.getQuantity() == 0) {
			logInvalidRequest(request);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		// Find user by name
		User user = null;
		try {
			user = userRepository.findByUsername(request.getUsername());
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/removeFromCart", "removeFromcart", "processing", error.getCode(), error.getDescription(), "User Repository",
					"findByUsername", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(user == null) {
			ErrorCode error = ErrorCode.USER_NAME_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/order/removeFromCart", "removeFromcart", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Find item by id
		Optional<Item> item = null;
		try {
			item = itemRepository.findById(request.getItemId());
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/removeFromCart", "removeFromcart", "processing", error.getCode(), error.getDescription(), "Item Repository",
					"findById", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(!item.isPresent()) {
			ErrorCode error = ErrorCode.ITEM_ID_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/order/removeFromCart", "removeFromcart", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Remove item
		Cart cart = user.getCart();
		final Item cartItem = item.get();
		IntStream.range(0, request.getQuantity())
				.forEach(i -> cart.removeItem(cartItem));

		// Save cart
		try {
			cartRepository.save(cart);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/removeFromCart", "removeFromcart", "processing", error.getCode(), error.getDescription(), "Cart Repository",
					"save", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/order/removeFromCart", "removeFromcart", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return cart
		return new ResponseEntity(cart, HttpStatus.OK);
	}
		
}
