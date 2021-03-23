package com.udacity.course4.controllers;

import java.util.List;
import java.util.Optional;

import com.udacity.course4.common.ErrorCode;
import com.udacity.course4.common.Logging;
import com.udacity.course4.model.persistence.repositories.ItemRepository;
import com.udacity.course4.model.persistence.Item;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	private static Logger logger = LoggerFactory.getLogger(ItemController.class);

	private HttpStatus statusNotFound = HttpStatus.NOT_FOUND;
	private HttpStatus statusServerError = HttpStatus.INTERNAL_SERVER_ERROR;
	private HttpStatus statusOk = HttpStatus.OK;
	private HttpStatus statusBadRequest = HttpStatus.BAD_REQUEST;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		// Log entry
		Logging.logMethodEntry(logger, "/api/item", "getItems", "entry", null, null, null);

		// Initialize
		List<Item> itemList = null;

		// Find All items
		try {
			itemList = itemRepository.findAll();
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/item", "getItems", "processing", error.getCode(), error.getDescription(), "Item Repository",
					"findAll", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// verify items
		if(itemList == null) {
			ErrorCode error = ErrorCode.ITEMS_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/item", "getItems", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/item", "getItems", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return items
		return new ResponseEntity(itemList, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/item/{id}", "getItemById", "entry", "id", id.toString(), null);

		// Validation
		if(id == null) {
			ErrorCode error = ErrorCode.ITEM_ID_NULL;
			Logging.logValidationOrProcessingError(logger, "/api/item/{id}", "getItemById", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		// Initializa
		Optional<Item> foundItem = null;

		// Find item
		try {
			foundItem = itemRepository.findById(id);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/item/{id}", "getItemById", "processing", error.getCode(), error.getDescription(), "Item Repository",
					"findById", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify item
		if(foundItem == null || !foundItem.isPresent()) {
			ErrorCode error = ErrorCode.ITEM_ID_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/item/{id}", "getItemById", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/item/{id}", "getItemById", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return item
		return new ResponseEntity(foundItem.get(), HttpStatus.OK);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		// Log entry
		Logging.logMethodEntry(logger, "/api/item/name/{name}", "getItemsByName", "entry", "name", name, null);

		// Validation
		/*
		if(name == null) {
			ErrorCode error = ErrorCode.ITEM_NAME_NULL;
			Logging.logValidationOrProcessingError(logger, "/api/item/name/{name}", "getItemsByName", "validation", error.getCode(), error.getDescription(),
					statusBadRequest.value(), statusBadRequest.getReasonPhrase());

			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		 */

		// Initialize
		List<Item> itemList = null;

		// Find Items
		try {
			itemList = itemRepository.findByName(name);
		}
		catch(Exception ex) {
			ErrorCode error = ErrorCode.DATABASE_REPOSITORY_ERROR;
			Logging.logDatabaseError(logger, "/api/item/name/{name}", "getItemsByName", "processing", error.getCode(), error.getDescription(), "Item Repository",
					"findByName", statusServerError.value(), statusServerError.getReasonPhrase());

			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Verify items
		if(itemList == null || itemList.isEmpty()) {
			ErrorCode error = ErrorCode.ITEMS_NOT_FOUND;
			Logging.logValidationOrProcessingError(logger, "/api/item/name/{name}", "getItemsByName", "processing", error.getCode(), error.getDescription(),
					statusNotFound.value(), statusNotFound.getReasonPhrase());

			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		// Log exit
		Logging.logMethodExit(logger, "/api/item/name/{name}", "getItemsByName", "exit", statusOk.value(), statusOk.getReasonPhrase());

		// Return items
		return new ResponseEntity(itemList, HttpStatus.OK);
	}
}
