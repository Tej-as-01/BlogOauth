package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.model.Blogs;
import com.example.demo.repository.BlogsRepository;

@ExtendWith(MockitoExtension.class)
public class BlogsServiceTest {

    @Mock
    private BlogsRepository blogsRepository;

    @InjectMocks
    private BlogsService blogsService;

    private Blogs blog1;
    private Blogs blog2;

    @BeforeEach
    public void setup() {
        blog1 = new Blogs();
        blog1.setId(1L);
        blog1.setTitle("First Message");
        blog1.setCreatorName("Tejas");
        blog1.setDate("14/09/2025");
        blog1.setContent("Hello there");

        blog2 = new Blogs();
        blog2.setId(2L);
        blog2.setTitle("Second Message");
        blog2.setCreatorName("Rakesh");
        blog2.setDate("14/09/2025");
        blog2.setContent("Hello there");
    }

    @Test
    @DisplayName("Unit-Test Getting All Blogs")
    public void getProductsTest() {
        when(blogsRepository.findAll()).thenReturn(new ArrayList<>(List.of(blog1, blog2)));

        List<Blogs> products = blogsService.getAllBlogs();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
        assertThat(products.get(0).getCreatorName()).isEqualTo("Tejas");
    }

    @Test
    @DisplayName("Unit-Test Get Blog By Id")
    public void getBlogByIdSuccessTest() throws ResourceNotFoundException {
        when(blogsRepository.findById(1L)).thenReturn(Optional.of(blog1));

        Blogs found = blogsService.getById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("First Message");
    }

    @Test
    @DisplayName("Unit-Test Get Blog By Id- Not Found")
    public void getBlogByIdFailureTest() {
        when(blogsRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> blogsService.getById(3L));
    }

    @Test
    @DisplayName("Unit-Test Create Blog")
    public void createBlogTest() {
        when(blogsRepository.save(blog1)).thenReturn(blog1);
        Blogs saved = blogsService.createBlogs(blog1);

        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("First Message");
        verify(blogsRepository, times(1)).save(blog1);
    }
    
    @Test
    @DisplayName("Unit-Test Update Blog - Success")
    public void updateBlogSuccessTest() {
        Blogs updatedBlog = new Blogs();
        updatedBlog.setTitle("Updated Title");
        updatedBlog.setCreatorName("Updated Name");
        updatedBlog.setDate("15/09/2025");
        updatedBlog.setContent("Updated Content");

        when(blogsRepository.findById(1L)).thenReturn(Optional.of(blog1));
        when(blogsRepository.save(any(Blogs.class))).thenReturn(blog1);

        blogsService.updateBlogs(1L, updatedBlog);

        assertThat(blog1.getTitle()).isEqualTo("Updated Title");
        assertThat(blog1.getCreatorName()).isEqualTo("Updated Name");
        assertThat(blog1.getDate()).isEqualTo("15/09/2025");
        assertThat(blog1.getContent()).isEqualTo("Updated Content");

        verify(blogsRepository, times(1)).save(blog1);
    }


    @Test
    @DisplayName("Unit Test-Delete Blog By Id - Success")
    public void deleteBlogByIdSuccessTest() throws ResourceNotFoundException {
        when(blogsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(blogsRepository).deleteById(1L);

        blogsService.deleteById(1L);

        verify(blogsRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Unit-Test Delete Blog By Id - Not Found")
    public void deleteBlogByIdFailureTest() {
        when(blogsRepository.existsById(3L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> blogsService.deleteById(3L));
    }

    @Test
    @DisplayName("Unit-Test Delete All Blogs")
    public void deleteAllBlogsTest() {
        doNothing().when(blogsRepository).deleteAll();

        blogsService.deleteAll();

        verify(blogsRepository, times(1)).deleteAll();
    }
}
