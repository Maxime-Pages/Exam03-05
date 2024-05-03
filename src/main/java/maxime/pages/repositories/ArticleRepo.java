package maxime.pages.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import maxime.pages.models.Article;

public interface ArticleRepo extends JpaRepository<Article, Long>{
    //Generate methods for CRUD operations on stock article

    Article save(Article entity);
    Optional<Article> findById(Long id);
    void deleteById(Long id);
    List<Article> findAll();

    
}
