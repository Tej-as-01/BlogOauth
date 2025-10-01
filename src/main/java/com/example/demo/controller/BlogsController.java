package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.service.BlogsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/blogs")
@Tag(name = "Blogs", description = "Operations related to blog posts")
public class BlogsController {

	@Autowired
	private BlogsService blogservice;

	@GetMapping()
	@Operation(summary="Getting all Blogs")
	public List<Blogs> getAllBlogs() {
		return blogservice.getAllBlogs();
	}

	@GetMapping("/{id}")
	@Operation(summary="Getting Blogs by ID")
	public Blogs getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
		return blogservice.getById(id);
	}

	@PostMapping("/post")
	@Operation(summary="Posting the Blogs")
	public ResponseEntity<Blogs> createBlogs(@RequestBody Blogs blog) {
		Blogs blogs=blogservice.createBlogs(blog);
		return ResponseEntity.status(HttpStatus.CREATED).body(blogs);
	}

	@PutMapping("/update/{id}")
	@Operation(summary="Updating the Blogs")
	public void updateBlogs(@PathVariable("id") Long id, @RequestBody Blogs blog)
	{
		blogservice.updateBlogs(id,blog);
	}
	
	
	
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary="Deleting Blogs by ID")
	public void deleteById(@PathVariable("id") Long id) throws ResourceNotFoundException {
		blogservice.deleteById(id);
	}

	@DeleteMapping("/delete")
	@Operation(summary="Deleting all Blogs")
	public void deleteAll() {
		blogservice.deleteAll();
	}

}
