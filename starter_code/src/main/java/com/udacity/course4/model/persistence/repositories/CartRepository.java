package com.udacity.course4.model.persistence.repositories;

import com.udacity.course4.model.persistence.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.course4.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
