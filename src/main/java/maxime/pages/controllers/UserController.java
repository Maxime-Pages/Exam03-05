package maxime.pages.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> validate(@RequestBody String code){ //No intruction given on how to validate code so I assume the code is always valid
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("users")
    public ResponseEntity<?> edit(@RequestBody String code){ //No intruction given on how to validate code so I assume the code is always valid
        return new ResponseEntity<>(HttpStatus.OK);
    }




    
}
