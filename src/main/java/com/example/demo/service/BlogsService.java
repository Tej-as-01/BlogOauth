package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;

@Service
public class BlogsService {

	@Autowired
	private BlogsRepository blogsRepository;

	public List<Blogs> getAllBlogs() {
		return blogsRepository.findAll();
	}

	public Blogs getById(Long id)  {
		return blogsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
	}

	public Blogs createBlogs(Blogs blog) {

		return blogsRepository.save(blog);

	}
public void updateBlogs(Long id, Blogs blog) {
		Optional<Blogs> result=blogsRepository.findById(id);
		
		if(result.isEmpty())
		{
			throw new ResourceNotFoundException("Blogs not found with id:"+id);
		}
		
		Blogs existingBlog=result.get();
		
		existingBlog.setTitle(blog.getTitle());
		existingBlog.setCreatorName(blog.getCreatorName());
		existingBlog.setDate(blog.getDate());
		existingBlog.setContent(blog.getContent());
		
		blogsRepository.save(existingBlog);
	}

	public void deleteById(Long id)  {
		if (!blogsRepository.existsById(id)) {
			throw new ResourceNotFoundException("Blog not found with id: " + id);
		}
		blogsRepository.deleteById(id);

	}

	public void deleteAll() {
		blogsRepository.deleteAll();

	}

	

}
