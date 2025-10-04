package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.model.Blogs;
import com.example.demo.repository.BlogsRepository;

/**
 * Service class for handling business logic related to blog operations.
 */
@Service
public class BlogsService {

	@Autowired
	private BlogsRepository blogsRepository;

	/**
	 * Retrieves all blog entries from the database.
	 * @return List of all blogs
	 */
	public List<Blogs> getAllBlogs() {
		return blogsRepository.findAll();
	}

	/**
	 * Retrieves a blog by its ID.
	 * @param id Blog ID
	 * @return Blog object
	 * @throws ResourceNotFoundException if blog is not found
	 */
	public Blogs getById(Long id) {
		return blogsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
	}

	/**
	 * Creates a new blog entry.
	 * @param blog Blog object to be saved
	 * @return Saved blog object
	 */
	public Blogs createBlogs(Blogs blog) {

		return blogsRepository.save(blog);

	}
	
	 /**
     * Updates an existing blog by its ID.
     * @param id Blog ID
     * @param blog Blog object with updated data
     * @throws ResourceNotFoundException if blog is not found
     */
	public void updateBlogs(Long id, Blogs blog) {
		Optional<Blogs> result = blogsRepository.findById(id);

		if (result.isEmpty()) {
			throw new ResourceNotFoundException("Blogs not found with id:" + id);
		}

		Blogs existingBlog = result.get();

		existingBlog.setTitle(blog.getTitle());
		existingBlog.setCreatorName(blog.getCreatorName());
		existingBlog.setDate(blog.getDate());
		existingBlog.setContent(blog.getContent());

		blogsRepository.save(existingBlog);
	}
	/**
     * Deletes a blog by its ID.
     * @param id Blog ID
     * @throws ResourceNotFoundException if blog is not found
     */
	public void deleteById(Long id) {
		if (!blogsRepository.existsById(id)) {
			throw new ResourceNotFoundException("Blog not found with id: " + id);
		}
		blogsRepository.deleteById(id);

	}
	
	/**
     * Deletes all blog entries from the database.
     */
	public void deleteAll() {
		blogsRepository.deleteAll();

	}

}
