package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.TestSecurityConfig;
import com.example.demo.repo.Blogs;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST and GET Blog")
    public void createAndGetBlogTest() {
        Blogs blog = new Blogs();
        blog.setTitle("Integration Blog");
        blog.setCreatorName("Tejas");
        blog.setDate("18/09/2025");
        blog.setContent("Testing post");

        ResponseEntity<Blogs> postResponse = restTemplate.postForEntity("/blogs/post", blog, Blogs.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Blogs created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Integration Blog");

        ResponseEntity<Blogs> getResponse = restTemplate.getForEntity("/blogs/" + created.getId(), Blogs.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Integration Blog");
    }

    @Test
    @DisplayName("PUT and GET Blog")
    public void updateBlogTest() {
        Blogs blog = new Blogs();
        blog.setTitle("Original Title");
        blog.setCreatorName("Author A");
        blog.setDate("18/09/2025");
        blog.setContent("Original Content");

        ResponseEntity<Blogs> postResponse = restTemplate.postForEntity("/blogs/post", blog, Blogs.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Blogs created = postResponse.getBody();
        assertThat(created).isNotNull();
        Long id = created.getId();

        Blogs updatedBlog = new Blogs();
        updatedBlog.setTitle("Updated Title");
        updatedBlog.setCreatorName("Author B");
        updatedBlog.setDate("19/09/2025");
        updatedBlog.setContent("Updated Content");

        HttpEntity<Blogs> requestEntity = new HttpEntity<>(updatedBlog);
        ResponseEntity<Void> putResponse = restTemplate.exchange("/blogs/update/" + id, HttpMethod.PUT, requestEntity, Void.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Blogs> getResponse = restTemplate.getForEntity("/blogs/" + id, Blogs.class);
        Blogs fetched = getResponse.getBody();
        assertThat(fetched).isNotNull();
        assertThat(fetched.getTitle()).isEqualTo("Updated Title");
        assertThat(fetched.getCreatorName()).isEqualTo("Author B");
    }

    @Test
    @DisplayName("DELETE Blog by ID")
    public void deleteBlogByIdTest() {
        Blogs blog = new Blogs();
        blog.setTitle("To Be Deleted");
        blog.setCreatorName("Author X");
        blog.setDate("18/09/2025");
        blog.setContent("Delete me");

        ResponseEntity<Blogs> postResponse = restTemplate.postForEntity("/blogs/post", blog, Blogs.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Blogs created = postResponse.getBody();
        assertThat(created).isNotNull();
        Long id = created.getId();

        restTemplate.delete("/blogs/delete/" + id);

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/blogs/" + id, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("DELETE All Blogs")
    public void deleteAllBlogsTest() {
        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog One");
        blog1.setCreatorName("User1");
        blog1.setDate("18/09/2025");
        blog1.setContent("Content One");

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog Two");
        blog2.setCreatorName("User2");
        blog2.setDate("18/09/2025");
        blog2.setContent("Content Two");

        ResponseEntity<Blogs> response1 = restTemplate.postForEntity("/blogs/post", blog1, Blogs.class);
        ResponseEntity<Blogs> response2 = restTemplate.postForEntity("/blogs/post", blog2, Blogs.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        restTemplate.delete("/blogs/delete");

        ResponseEntity<Blogs[]> response = restTemplate.getForEntity("/blogs", Blogs[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
