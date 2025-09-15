package com.example.demo.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
class BlogsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        blogsRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /blogs - Should return all blogs")
    void testGetAllBlogs() throws Exception {
        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog One");
        blog1.setCreatorName("Tejas");
        blog1.setDate("15/09/2025");
        blog1.setContent("Content One");

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog Two");
        blog2.setCreatorName("Rakesh");
        blog2.setDate("15/09/2025");
        blog2.setContent("Content Two");

        blogsRepository.saveAll(List.of(blog1, blog2));

        mockMvc.perform(get("/blogs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("Blog One"))
            .andExpect(jsonPath("$[1].title").value("Blog Two"));
    }

    @Test
    @DisplayName("GET /blogs/{id} - Should return blog by ID")
    void testGetBlogById() throws Exception {
        Blogs blog = new Blogs();
        blog.setTitle("Find Me");
        blog.setCreatorName("Tejas");
        blog.setDate("15/09/2025");
        blog.setContent("Find by ID");

        Blogs saved = blogsRepository.save(blog);

        mockMvc.perform(get("/blogs/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Find Me"))
            .andExpect(jsonPath("$.creatorName").value("Tejas"));
    }

    @Test
    @DisplayName("POST /blogs - Should create a new blog")
    void testCreateBlog() throws Exception {
        Blogs blog = new Blogs();
        blog.setTitle("New Blog");
        blog.setCreatorName("Anjali");
        blog.setDate("15/09/2025");
        blog.setContent("Fresh content");

        String json = objectMapper.writeValueAsString(blog);

        mockMvc.perform(post("/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("New Blog"));

        List<Blogs> allBlogs = blogsRepository.findAll();
        assertThat(allBlogs).hasSize(1);
        assertThat(allBlogs.get(0).getCreatorName()).isEqualTo("Anjali");
    }

    @Test
    @DisplayName("DELETE /blogs/delete/{id} - Should delete blog by ID")
    void testDeleteBlogById() throws Exception {
        Blogs blog = new Blogs();
        blog.setTitle("Delete Me");
        blog.setCreatorName("Ravi");
        blog.setDate("15/09/2025");
        blog.setContent("To be deleted");

        Blogs saved = blogsRepository.save(blog);

        mockMvc.perform(delete("/blogs/delete/" + saved.getId()))
            .andExpect(status().isOk());

        assertThat(blogsRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /blogs/delete - Should delete all blogs")
    void testDeleteAllBlogs() throws Exception {
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

        blogsRepository.saveAll(List.of(blog1, blog2));

        mockMvc.perform(delete("/blogs/delete"))
            .andExpect(status().isOk());

        assertThat(blogsRepository.findAll()).isEmpty();
    }
}

