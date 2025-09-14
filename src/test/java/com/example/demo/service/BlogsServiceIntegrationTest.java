package com.example.demo.service;

import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;
import com.example.demo.ResourceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BlogsServiceIntegrationTest {

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private BlogsRepository blogsRepository;

    @Test
    @DisplayName("Integration-Test create blog")
    void testCreateBlog() {
        Blogs blog = new Blogs();
        blog.setTitle("Create Blog");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content for create test");

        Blogs saved = blogsService.createBlogs(blog);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Create Blog");
    }

    @Test
    @DisplayName("Integration-Test get blog by id")
    void testGetBlogById() throws ResourceNotFoundException {
        Blogs blog = new Blogs();
        blog.setTitle("GetById Blog");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content for getById test");

        Blogs saved = blogsService.createBlogs(blog);
        Blogs fetched = blogsService.getById(saved.getId());

        assertThat(fetched).isNotNull();
        assertThat(fetched.getTitle()).isEqualTo("GetById Blog");
    }

    @Test
    @DisplayName("Integration-Test get all blogs")
    void testGetAllBlogs() {
        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog One");
        blog1.setCreatorName("Tejas");
        blog1.setDate("14/09/2025");
        blog1.setContent("Content One");

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog Two");
        blog2.setCreatorName("Tejas");
        blog2.setDate("14/09/2025");
        blog2.setContent("Content Two");

        blogsService.createBlogs(blog1);
        blogsService.createBlogs(blog2);

        List<Blogs> allBlogs = blogsService.getAllBlogs();
        assertThat(allBlogs).hasSizeGreaterThanOrEqualTo(2);
  
    }

    @Test
    @DisplayName("Integration-Test delete blog by id")
    void testDeleteBlogById() throws ResourceNotFoundException {
        Blogs blog = new Blogs();
        blog.setTitle("Delete By Id Blog");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content for deleteById test");

        Blogs saved = blogsService.createBlogs(blog);
        blogsService.deleteById(saved.getId());

      boolean exists = blogsRepository.existsById(saved.getId());
      assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Integration-Test delete all blogs")
    void testDeleteAllBlogs() {
        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog One");
        blog1.setCreatorName("Tejas");
        blog1.setDate("14/09/2025");
        blog1.setContent("Content One");

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog Two");
        blog2.setCreatorName("Tejas");
        blog2.setDate("14/09/2025");
        blog2.setContent("Content Two");

        blogsService.createBlogs(blog1);
        blogsService.createBlogs(blog2);

        blogsService.deleteAll();

        List<Blogs> allBlogs = blogsService.getAllBlogs();
        assertThat(allBlogs).isEmpty();
    }
}
