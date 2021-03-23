package com.udacity.course4.controllers;

import com.udacity.course4.common.*;
import com.udacity.course4.model.persistence.*;
import com.udacity.course4.model.persistence.repositories.*;
import com.udacity.course4.model.requests.CreateUserRequest;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	private HttpStatus statusNotFound = HttpStatus.NOT_FOUND;
	private HttpStatus statusServerError = HttpStatus.INTERNAL_SERVER_ERROR;
	private HttpStatus statusOk = HttpStatus.OK;
	private HttpStatus statusBadRequest = HttpStatus.BAD_REQUEST;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/user/id/{id}", "findById", "entry", "id", id.toString(), null);

		// Initialize
		Optional<User> foundUser = null;

		// Find User by id
		try {
			foundUser = userRepository.findById(id);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/user/id/{id}", "findById", "processing", error.getCode(), error.getDescription(), "User Repository",
									"findById", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify User
		if(!foundUser.isPresent()) {
			ErrorCode error = ErrorCode.USER_ID_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/user/id/{id}", "findById", "processing", error.getCode(), error.getDescription(),
													statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/user/id/{id}", "findById", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return user
		return new ResponseEntity(foundUser.get(), HttpStatus.OK);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/user/{username}", "findByUserName", "entry", "username", username, null);

		// Initialize
		User user = null;

		// Find user by name
		try {
			user = userRepository.findByUsername(username);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/user/{username}", "findByUserName", "processing", error.getCode(), error.getDescription(), "User Repository",
									"findByUsername", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify found user
		if(user == null) {
			ErrorCode error = ErrorCode.USER_NAME_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/user/{username}", "findByUserName", "processing", error.getCode(), error.getDescription(),
													statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/user/{username}", "findByUserName", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return user
		return new ResponseEntity(user, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/user/create", "createUser", "entry", "username", request.getUsername(), null);

		// Validation
		/* Password Requirements - 1. Password must be of atleast of 8 characters long,  2. Password must not start with numeric, 3. Password must not be of text Password */
		String password = request.getPassword();
		if(password.length() < 8 || Character.isDigit(password.charAt(0)) || password.equalsIgnoreCase("password")) {
			ErrorCode error = ErrorCode.PASSWORD_INVALID;
			Logging.logValidationOrProcessingError(logger, "/api/user/create", "createUser", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		// Validate Confirm Password
		String confirmPassword = request.getConfirmPassword();
		if(password.length() != confirmPassword.length() || !password.equals(confirmPassword)) {
			ErrorCode error = ErrorCode.CONFIRM_PASSWORD_INVALID;
			Logging.logValidationOrProcessingError(logger, "/api/user/create", "createUser", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		// Create new user and cart
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
		Cart cart = new Cart();

		// Save cart
		try {
			cartRepository.save(cart);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/user/create", "createUser", "processing", error.getCode(), error.getDescription(), "Cart Repository",
						"save", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		user.setCart(cart);

		// Save user
		try {
			userRepository.save(user);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/user/create", "createUser", "processing", error.getCode(), error.getDescription(), "User Repository",
						"save", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Post Processing
		MessageCode message = MessageCode.USER_CREATION_SUCCESS;
		Logging.logSuccessMessage(logger, "/api/user/create", "createUser", "post processing", message.getCode(), message.getDescription());

		// Log exit
		Logging.logMethodExit(logger, "/api/user/create", "createUser", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return user
		return new ResponseEntity(user, HttpStatus.OK);
	}
	
}
