package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BlogsServiceIntegrationTest {

    @Autowired
    private BlogsService blogsService;

    @Autowired
    private BlogsRepository blogsRepository;

    @BeforeEach
    public void clearDatabase() {
        blogsRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration-Test Save Blog")
    public void saveBlogTest() {
        Blogs blog = new Blogs();
        blog.setTitle("Integration Blog");
        blog.setCreatorName("Tejas");
        blog.setDate("15/09/2025");
        blog.setContent("Testing save operation");

        Blogs saved = blogsService.createBlogs(blog);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Integration Blog");
    }

    @Test
    @DisplayName("Integration-Test Get All Blogs ")
    public void getAllBlogsTest() {
        Blogs blog = new Blogs();
        blog.setTitle("Another Blog");
        blog.setCreatorName("Rakesh");
        blog.setDate("15/09/2025");
        blog.setContent("Testing retrieval");

        blogsService.createBlogs(blog);

        List<Blogs> blogs = blogsService.getAllBlogs();

        boolean found = blogs.stream()
            .anyMatch(b -> b.getTitle().equals("Another Blog"));

        assertThat(found).isTrue();
    }

    @Test
    @DisplayName("Integration-Test Get Blog By ID")
    public void getBlogByIdTest() throws ResourceNotFoundException {
        Blogs blog = new Blogs();
        blog.setTitle("Find Me");
        blog.setCreatorName("Anjali");
        blog.setDate("15/09/2025");
        blog.setContent("Find by ID test");

        Blogs saved = blogsService.createBlogs(blog);
        Blogs found = blogsService.getById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Find Me");
    }

    @Test
    @DisplayName("Integration-Test Get Blog By Missing ID - Should Throw Exception")
    public void getBlogByMissingIdTest() {
        Long missingId = 999L;

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            blogsService.getById(missingId);
        });

        assertThat(ex.getMessage()).contains("Blog not found with id");
    }
    @Test
    @DisplayName("Integration-Test Update Blog")
    public void updateBlogTest() throws ResourceNotFoundException {
        
        Blogs original = new Blogs();
        original.setTitle("Original Title");
        original.setCreatorName("Original Author");
        original.setDate("15/09/2025");
        original.setContent("Original Content");

        Blogs saved = blogsService.createBlogs(original);
        Long id = saved.getId();

       
        Blogs updated = new Blogs();
        updated.setTitle("Updated Title");
        updated.setCreatorName("Updated Author");
        updated.setDate("16/09/2025");
        updated.setContent("Updated Content");

       
        blogsService.updateBlogs(id, updated);

       
        Blogs result = blogsService.getById(id);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getCreatorName()).isEqualTo("Updated Author");
        assertThat(result.getDate()).isEqualTo("16/09/2025");
        assertThat(result.getContent()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("Integration-Test Delete Blog By ID")
    public void deleteBlogByIdTest() throws ResourceNotFoundException {
        Blogs blog = new Blogs();
        blog.setTitle("Delete Me");
        blog.setCreatorName("Ravi");
        blog.setDate("15/09/2025");
        blog.setContent("Delete test");

        Blogs saved = blogsService.createBlogs(blog);
        Long id = saved.getId();

        blogsService.deleteById(id);

        assertThrows(ResourceNotFoundException.class, () -> blogsService.getById(id));
    }

    @Test
    @DisplayName("Integration-Test Delete All Blogs")
    public void deleteAllBlogsTest() {
        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog One");
        blog1.setCreatorName("User1");
        blog1.setDate("15/09/2025");
        blog1.setContent("Content One");

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog Two");
        blog2.setCreatorName("User2");
        blog2.setDate("15/09/2025");
        blog2.setContent("Content Two");

        blogsService.createBlogs(blog1);
        blogsService.createBlogs(blog2);

        blogsService.deleteAll();

        List<Blogs> blogs = blogsService.getAllBlogs();
        assertThat(blogs).isEmpty();
    }
}
