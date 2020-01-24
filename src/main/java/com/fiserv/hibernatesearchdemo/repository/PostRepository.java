package com.fiserv.hibernatesearchdemo.repository;

import com.fiserv.hibernatesearchdemo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
