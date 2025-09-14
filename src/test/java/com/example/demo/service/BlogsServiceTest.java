package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.demo.ResourceNotFoundException;
import com.example.demo.repo.Blogs;
import com.example.demo.repo.BlogsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BlogsServiceTest {

    @Mock
    private BlogsRepository blogsRepository;

    @InjectMocks
    private BlogsService blogsService;

    private Blogs blog1;
    private Blogs blog2;

    @BeforeEach
    void setUp() {
        blog1 = new Blogs();
        blog1.setId(1L);
        blog1.setTitle("Test Blog 1");
        blog1.setCreatorName("Tejas");
        blog1.setDate("14/09/2025");
        blog1.setContent("Content 1");

        blog2 = new Blogs();
        blog2.setId(2L);
        blog2.setTitle("Test Blog 2");
        blog2.setCreatorName("Tejas");
        blog2.setDate("14/09/2025");
        blog2.setContent("Content 2");
    }

    @Test
    @DisplayName("Unit-Test getAllBlogs")
    void testGetAllBlogs() {
        when(blogsRepository.findAll()).thenReturn(Arrays.asList(blog1, blog2));

        List<Blogs> blogs = blogsService.getAllBlogs();

        assertThat(blogs).hasSize(2);
        assertThat(blogs).contains(blog1, blog2);
        verify(blogsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Unit-Test getById - success")
    void testGetById_Success() throws ResourceNotFoundException {
        when(blogsRepository.findById(1L)).thenReturn(Optional.of(blog1));

        Blogs blog = blogsService.getById(1L);

        assertThat(blog).isNotNull();
        assertThat(blog.getTitle()).isEqualTo("Test Blog 1");
        verify(blogsRepository, times(1)).findById(1L);
    }


    @Test
    @DisplayName("Unit-Test createBlogs")
    void testCreateBlogs() {
        when(blogsRepository.save(blog1)).thenReturn(blog1);

        Blogs saved = blogsService.createBlogs(blog1);

        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Blog 1");
        verify(blogsRepository, times(1)).save(blog1);
    }

    @Test
    @DisplayName("Unit-Test deleteById ")
    void testDeleteById_Success() throws ResourceNotFoundException {
        when(blogsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(blogsRepository).deleteById(1L);

        blogsService.deleteById(1L);

        verify(blogsRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Unit-Test deleteAll")
    void testDeleteAll() {
        doNothing().when(blogsRepository).deleteAll();

        blogsService.deleteAll();

        verify(blogsRepository, times(1)).deleteAll();
    }
}
