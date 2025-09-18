package com.example.demo.controller;


import com.example.demo.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.service.BlogsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogsController.class)
@AutoConfigureMockMvc(addFilters=false) 
class BlogsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogsService blogservice;

    @Test
    @DisplayName("Unit-Test GET /blogs")
    void testGetAllBlogs() throws Exception {
        Blogs blog1 = new Blogs();
        blog1.setId(1L);
        blog1.setTitle("Blog 1");
        blog1.setCreatorName("Tejas");
        blog1.setDate("14/09/2025");
        blog1.setContent("Content 1");

        Blogs blog2 = new Blogs();
        blog2.setId(2L);
        blog2.setTitle("Blog 2");
        blog2.setCreatorName("Tejas");
        blog2.setDate("14/09/2025");
        blog2.setContent("Content 2");

        List<Blogs> blogs = Arrays.asList(blog1, blog2);
        when(blogservice.getAllBlogs()).thenReturn(blogs);

        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Blog 1"))
                .andExpect(jsonPath("$[1].title").value("Blog 2"));

        verify(blogservice, times(1)).getAllBlogs();
    }

    @Test
    @DisplayName("Unit-Test GET /blogs/{id} - success")
    void testGetBlogById_Success() throws Exception {
        Blogs blog = new Blogs();
        blog.setId(1L);
        blog.setTitle("Blog 1");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content 1");

        when(blogservice.getById(1L)).thenReturn(blog);

        mockMvc.perform(get("/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Blog 1"))
                .andExpect(jsonPath("$.creatorName").value("Tejas"));

        verify(blogservice, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Unit-Test GET /blogs/{id} - not found")
    void testGetBlogById_NotFound() throws Exception {
        when(blogservice.getById(1L)).thenThrow(new ResourceNotFoundException("Blog not found"));

        mockMvc.perform(get("/blogs/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Blog not found"));

        verify(blogservice, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Unit-Test POST /blogs")
    void testCreateBlog() throws Exception {
        Blogs blog = new Blogs();
        blog.setId(1L);
        blog.setTitle("Blog 1");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Content 1");

        when(blogservice.createBlogs(any(Blogs.class))).thenReturn(blog);

        String json = """
                {
                  "title": "Blog 1",
                  "creatorName": "Tejas",
                  "date": "14/09/2025",
                  "content": "Content 1"
                }
                """;

        mockMvc.perform(post("/blogs/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Blog 1"));

        verify(blogservice, times(1)).createBlogs(any(Blogs.class));
    }
    
    @Test
    @DisplayName("Unit-Test PUT /blogs/update/{id}")
    void testUpdateBlog() throws Exception {
        Blogs updatedBlog = new Blogs();
        updatedBlog.setId(1L);
        updatedBlog.setTitle("Updated Title");
        updatedBlog.setCreatorName("Updated Author");
        updatedBlog.setDate("15/09/2025");
        updatedBlog.setContent("Updated Content");

        doNothing().when(blogservice).updateBlogs(eq(1L), any(Blogs.class));

        String json = """
                {
                  "title": "Updated Title",
                  "creatorName": "Updated Author",
                  "date": "15/09/2025",
                  "content": "Updated Content"
                }
                """;

        mockMvc.perform(put("/blogs/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(blogservice, times(1)).updateBlogs(eq(1L), any(Blogs.class));
    }


    @Test
    @DisplayName("Unit-Test DELETE /blogs/delete/{id}")
    void testDeleteBlogById() throws Exception {
        doNothing().when(blogservice).deleteById(1L);

        mockMvc.perform(delete("/blogs/delete/1"))
                .andExpect(status().isOk());

        verify(blogservice, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Unit-Test DELETE /blogs/delete")
    void testDeleteAllBlogs() throws Exception {
        doNothing().when(blogservice).deleteAll();

        mockMvc.perform(delete("/blogs/delete"))
                .andExpect(status().isOk());

        verify(blogservice, times(1)).deleteAll();
    }
}
