package com.example.demo.controller;

import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import jakarta.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional  
class BlogsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogsRepository blogsRepository;

    @Test
    @DisplayName("Integration-Test GET /blogs")
    void testGetAllBlogs() throws Exception {
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

        blogsRepository.save(blog1);
        blogsRepository.save(blog2);

        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Blog One"))
                .andExpect(jsonPath("$[1].title").value("Blog Two"));
    }

    @Test
    @DisplayName("Integration-Test GET /blogs/{id}")
    void testGetBlogById() throws Exception {
        Blogs blog = new Blogs();
        blog.setTitle("Blog Test");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content Test");

        Blogs saved = blogsRepository.save(blog);

        mockMvc.perform(get("/blogs/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Blog Test"))
                .andExpect(jsonPath("$.creatorName").value("Tejas"));
    }

    @Test
    @DisplayName("Integration-Test POST /blogs")
    void testCreateBlog() throws Exception {
        String json = """
                {
                  "title": "New Blog",
                  "creatorName": "Tejas",
                  "date": "14/09/2025",
                  "content": "New Content"
                }
                """;

        mockMvc.perform(post("/blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Blog"));

        List<Blogs> allBlogs = blogsRepository.findAll();
        assertThat(allBlogs).hasSize(1);
        assertThat(allBlogs.get(0).getTitle()).isEqualTo("New Blog");
    }

    @Test
    @DisplayName("Integration-Test DELETE /blogs/delete/{id}")
    void testDeleteBlogById() throws Exception {
        Blogs blog = new Blogs();
        blog.setTitle("To Delete");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Delete Content");

        Blogs saved = blogsRepository.save(blog);

        mockMvc.perform(delete("/blogs/delete/" + saved.getId()))
                .andExpect(status().isOk());

        boolean exists = blogsRepository.existsById(saved.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Integration-Test DELETE /blogs/delete")
    void testDeleteAllBlogs() throws Exception {
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

        blogsRepository.save(blog1);
        blogsRepository.save(blog2);

        mockMvc.perform(delete("/blogs/delete"))
                .andExpect(status().isOk());

        List<Blogs> allBlogs = blogsRepository.findAll();
        assertThat(allBlogs).isEmpty();
    }
}
