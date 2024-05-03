package maxime.pages.serviceimplem;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import maxime.pages.models.Article;
import maxime.pages.repositories.ArticleRepo;
import maxime.pages.services.ArticleService;

@Service
public class ArticleImplem implements ArticleService {
    
    @Autowired
    ArticleRepo articleRepo;

    @Override
    public List<Article> getAllArticles() {
        return articleRepo.findAll();
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepo.findById(id);
    }

    @Override
    public Article createArticle(@Valid Article entity) {
        return articleRepo.save(entity);
    }

    @Override
    public Article updateArticle(Article entity) {
        return articleRepo.save(entity);
    }
    
}
