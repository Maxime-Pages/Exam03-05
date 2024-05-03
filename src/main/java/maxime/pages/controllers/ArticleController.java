package maxime.pages.controllers;

import java.util.Map;
import java.util.Optional;
import java.io.Console;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import maxime.pages.models.Article;
import maxime.pages.models.User;
import maxime.pages.services.ArticleService;
import maxime.pages.services.AuthService;
import maxime.pages.services.UserService;

@RestController
@RequestMapping("api")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @GetMapping("products")
    public ResponseEntity<?> getProducts(){
        List<Article> articles = articleService.getAllArticles();
        return new ResponseEntity<>(articles,HttpStatus.OK);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable(value="id") Long id){
        Optional<Article> article = articleService.getArticleById(id);
        if(article.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(article.get(),HttpStatus.OK);
    }

    @PostMapping("products")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createProduct(@RequestHeader("Authorization") String token, @RequestBody Article entity){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        entity = articleService.createArticle(entity);
        user.get().AddArticle(entity);
        userService.updateUser(user.get());
        return new ResponseEntity<>(entity,HttpStatus.CREATED);
    }

    @PutMapping("products/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String token, @PathVariable(value="id") Long id, @RequestBody Article entity){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        if(user.get().getArticles().stream().noneMatch(article -> article.getId() == id))
        {
            return new ResponseEntity<>("Article is not yours or doesn't exist",HttpStatus.UNAUTHORIZED);
        }
        entity.setId(id);
        entity = articleService.updateArticle(entity);
        return new ResponseEntity<>(entity,HttpStatus.CREATED);
        
    }

    @DeleteMapping("products/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable(value="id") Long id){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        if(user.get().getArticles().stream().noneMatch(article -> article.getId() == id))
        {
            return new ResponseEntity<>("Article is not yours or doesn't exist",HttpStatus.UNAUTHORIZED);
        }
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("stock/entry")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> stockEntry(@RequestHeader("Authorization") String token, @RequestBody Map<String,Object> request){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        Long id = Long.valueOf(request.get("id").toString());
        int quantity = Integer.valueOf(request.get("quantity").toString());
        Optional<Article> entity = articleService.getArticleById(id);
        if(user.get().getArticles().stream().noneMatch(article -> article.getId() == id))
        {
            return new ResponseEntity<>("Article is not yours or doesn't exist",HttpStatus.NOT_FOUND);
        }
        entity.get().setStock(entity.get().getStock() + quantity);
        articleService.updateArticle(entity.get());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("stock/exit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> stockExit(@RequestHeader("Authorization") String token, @Valid @RequestBody Map<String,Object> request){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        Long id = Long.valueOf(request.get("id").toString());
        int quantity = Integer.valueOf(request.get("quantity").toString());
        Optional<Article> entity = articleService.getArticleById(id);
        if(user.get().getArticles().stream().noneMatch(article -> article.getId() == id))
        {
            return new ResponseEntity<>("Article is not yours or doesn't exist",HttpStatus.NOT_FOUND);
        }
        if(entity.get().getStock() < quantity)
        {
            return new ResponseEntity<>("Not enough stock",HttpStatus.BAD_REQUEST);
        }
        entity.get().setStock(entity.get().getStock() - quantity);
        articleService.updateArticle(entity.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("reports/inventory")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getInventoryReport(@RequestHeader("Authorization") String token){
        Optional<User> user = userService.getUserByEmail(authService.getEmailFromToken(token));
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User not found",HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user.get().getArticles(),HttpStatus.OK);
    }

}
