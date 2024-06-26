package maxime.pages.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import maxime.pages.config.applicationConfig;
import maxime.pages.models.User;
import maxime.pages.services.ArticleService;
import maxime.pages.services.AuthService;
import maxime.pages.services.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @Autowired
    AuthService authService;

    @PostMapping("users/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String password = request.get("password");
        Optional<User> user = userService.getUserByEmail(email);
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        if(!user.get().getValid())
        {
            return new ResponseEntity<>("User is not validated yet",HttpStatus.FORBIDDEN);
        }
        String jwt = authService.loginUser(user.get(),password);
        if (jwt == null)
            return new ResponseEntity<>("Incorrect Password",HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @PostMapping("users/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String,String> request){ //No intruction given on how to validate code so I assume the code is always valid
        String email = request.get("email");
        String code = request.get("code");
        if (code == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!code.equals(generateRandomCode())){
            return new ResponseEntity<>("Code is incorrect",HttpStatus.FORBIDDEN);
        }
        Optional<User> user = userService.getUserByEmail(email);
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        user.get().setValid(true);
        userService.updateUser(user.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generateRandomCode(){
        return "42"; //42 was randomly generated by me
    }

    @PutMapping("users")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> edit(@RequestHeader(name="Authorization") String bearerToken, @RequestBody User entity){ //No intruction given on how to validate code so I assume the code is always valid
        if (entity.getEmail() == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.getUserByEmail(entity.getEmail());
        if(user.isEmpty())
        {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }

        if(!user.get().getEmail().equals(authService.getEmailFromToken(bearerToken)))
        {
            return new ResponseEntity<>("You can only edit your own account",HttpStatus.FORBIDDEN);
        }
        
        entity.setId(user.get().getId());
        entity.setPassword(authService.encryptPW(entity.getPassword()));
        return new ResponseEntity<>(userService.updateUser(entity),HttpStatus.OK);
    }




    
}
