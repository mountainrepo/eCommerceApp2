package com.udacity.course4.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.course4.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
