package maxime.pages.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import maxime.pages.models.Article;

@Service
public interface ArticleService {

    List<Article> getAllArticles();

    Optional<Article> getArticleById(Long id);

    Article createArticle( Article entity);

    Article updateArticle(Article entity);
    
}
