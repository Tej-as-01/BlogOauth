package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs,Long> {

}
