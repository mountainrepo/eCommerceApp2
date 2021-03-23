package com.udacity.course4.model.persistence.repositories;

import java.util.List;

import com.udacity.course4.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.course4.model.persistence.User;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
