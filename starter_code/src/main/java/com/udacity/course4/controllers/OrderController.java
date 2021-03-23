package com.udacity.course4.controllers;

import java.util.List;

import com.udacity.course4.common.ErrorCode;
import com.udacity.course4.common.Logging;
import com.udacity.course4.common.MessageCode;
import com.udacity.course4.model.persistence.UserOrder;
import com.udacity.course4.model.persistence.repositories.UserRepository;
import com.udacity.course4.model.persistence.User;
import com.udacity.course4.model.persistence.repositories.OrderRepository;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	private static Logger logger = LoggerFactory.getLogger(OrderController.class);

	private HttpStatus statusNotFound = HttpStatus.NOT_FOUND;
	private HttpStatus statusServerError = HttpStatus.INTERNAL_SERVER_ERROR;
	private HttpStatus statusOk = HttpStatus.OK;
	private HttpStatus statusBadRequest = HttpStatus.BAD_REQUEST;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/order/submit/{username}", "submit", "entry", null, null, null);

		// Validation
		/*
		if(username == null) {
			ErrorCode error = ErrorCode.USER_NAME_NULL;
			Logging.logValidationOrProcessingError(logger, "/api/order/submit/{username}", "submit", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		 */

		// Initialize
		User user = null;

		// Find by username
		try {
			user = userRepository.findByUsername(username);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/submit/{username}", "findByUsername", "processing", error.getCode(), error.getDescription(), "User Repository",
					"findByUsername", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify user
		if(user == null) {
			ErrorCode error = ErrorCode.USER_NAME_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/order/submit/{username}", "findByUsername", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Save
		UserOrder order = UserOrder.createFromCart(user.getCart());

		try {
			orderRepository.save(order);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/submit/{username}", "findByUsername", "processing", error.getCode(), error.getDescription(), "Order Repository",
					"save", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Log Successful order creation
		MessageCode message = MessageCode.ORDER_CREATION_SUCCESS;
		Logging.logSuccessMessage(logger, "/api/order/submit/{username}", "findByUsername", "post processing", message.getCode(), message.getDescription());

		// Log exit
		Logging.logMethodExit(logger, "/api/order/submit/{username}", "findByUsername", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return order
		return new ResponseEntity(order, HttpStatus.OK);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/order/history/{username}", "getOrdersForUser", "entry", null, null, null);

		// Validation
		/*
		if(username == null) {
			ErrorCode error = ErrorCode.USER_NAME_NULL;
			Logging.logValidationOrProcessingError(logger, "/api/order/history/{username}", "getOrdersForUser", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		 */

		// Initialize
		User user = null;

		// Find user by name
		try {
			user = userRepository.findByUsername(username);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/history/{username}", "getOrdersForUser", "processing", error.getCode(), error.getDescription(), "User Repository",
					"findByUsername", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify user
		if(user == null) {
			ErrorCode error = ErrorCode.USER_NAME_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/order/history/{username}", "getOrdersForUser", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Find orders by user
		List<UserOrder> orderList = null;
		try {
			orderList = orderRepository.findByUser(user);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/order/history/{username}", "getOrdersForUser", "processing", error.getCode(), error.getDescription(), "Order Repository",
					"findByUser", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/order/submit/{username}", "getOrdersForUser", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return orders
		return new ResponseEntity(orderList, HttpStatus.OK);
	}
}
