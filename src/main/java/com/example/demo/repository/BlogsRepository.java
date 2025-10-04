package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Blogs;

/**
 * Repository interface for managing {@link Blogs} entities.
 * Provides CRUD operations and query methods via Spring Data JPA.
 */
public interface BlogsRepository extends JpaRepository<Blogs,Long> {

}
