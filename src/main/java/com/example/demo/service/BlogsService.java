package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;

@Service
public class BlogsService {

	@Autowired
	private BlogsRepository blogsRepository;

	public List<Blogs> getAllBlogs() {
		return blogsRepository.findAll();
	}

	public Blogs getById(Long id) throws ResourceNotFoundException {
		return blogsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
	}

	public Blogs createBlogs(Blogs blog) {

		return blogsRepository.save(blog);

	}

	public void deleteById(Long id) throws ResourceNotFoundException {
		if (!blogsRepository.existsById(id)) {
			throw new ResourceNotFoundException("Blog not found with id: " + id);
		}
		blogsRepository.deleteById(id);

	}

	public void deleteAll() {
		blogsRepository.deleteAll();

	}

}
