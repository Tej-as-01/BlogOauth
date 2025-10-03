package com.example.demo.repo;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.model.Blogs;
import com.example.demo.repository.BlogsRepository;

@DataJpaTest
@ActiveProfiles("mysql-test")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class BlogRepositoryTest {

	@Autowired
	private BlogsRepository blogsRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	@DisplayName("Integration-Test Save blogs")
	public void testSaveBlogs()
	{
		Blogs blog=new Blogs();
		blog.setTitle("First Message");
		blog.setCreatorName("Tejas");
		blog.setDate("14/09/2025");
		blog.setContent("Hello there");
		
		Blogs saved=blogsRepository.save(blog);
		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isNotNull();
	}
	

    @Test
    @DisplayName("Integration-Test Find blog by ID")
    public void testFindById() {
        Blogs blog = new Blogs();
        blog.setTitle("Find Me");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("Looking for this");

        Blogs saved = entityManager.persistAndFlush(blog);
        Optional<Blogs> found = blogsRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Find Me");
    }

    @Test
    @DisplayName("Integration-Test Find all blogs")
    public void testFindAll() {
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

        entityManager.persist(blog1);
        entityManager.persist(blog2);
        entityManager.flush();

        List<Blogs> allBlogs = blogsRepository.findAll();
        assertThat(allBlogs).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Integration-Test Delete blog by ID")
    public void testDeleteById() {
        Blogs blog = new Blogs();
        blog.setTitle("Delete Me");
        blog.setCreatorName("Tejas");
        blog.setDate("14/09/2025");
        blog.setContent("To be deleted");

        Blogs saved = entityManager.persistAndFlush(blog);
        blogsRepository.deleteById(saved.getId());

        Optional<Blogs> found = blogsRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Integration-Test Delete all blogs")
    public void testDeleteAll() {
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

        entityManager.persist(blog1);
        entityManager.persist(blog2);
        entityManager.flush();

        blogsRepository.deleteAll();
        List<Blogs> allBlogs = blogsRepository.findAll();
        assertThat(allBlogs).isEmpty();
    }
}
