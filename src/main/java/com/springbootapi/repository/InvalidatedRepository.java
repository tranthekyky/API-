package com.springbootapi.repository;

import com.springbootapi.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvalidatedRepository extends JpaRepository<InvalidatedToken , String> {
}
