package com.fiserv.hibernatesearchdemo.repository;

import com.fiserv.hibernatesearchdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
