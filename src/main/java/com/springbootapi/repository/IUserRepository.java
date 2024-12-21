package com.springbootapi.repository;

import com.springbootapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
