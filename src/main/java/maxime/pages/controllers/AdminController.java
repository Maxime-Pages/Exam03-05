package maxime.pages.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maxime.pages.models.Admin;
import maxime.pages.models.User;
import maxime.pages.services.AdminService;
import maxime.pages.services.AuthService;
import maxime.pages.services.UserService;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String password = request.get("password");
        if(email == null || password ==  null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(password);
        return new ResponseEntity<>(authService.registerAdmin(admin),HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String password = request.get("password");
        Optional<Admin> admin = adminService.getAdmingByEmail(email);
        if(admin.isEmpty())
        {
            return new ResponseEntity<>("Admin does not exist",HttpStatus.NOT_FOUND);
        }
        String jwt = authService.loginAdmin(admin.get(),password);
        if (jwt == null)
            return new ResponseEntity<>("Incorrect Password",HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @PostMapping("users")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String,String> request){
        String email = request.get("email");
        String password = request.get("password");
        String fullname = request.get("fullname");
        if(email == null || password ==  null || fullname == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullname(fullname);
        return new ResponseEntity<>(authService.registerUser(user),HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") String id){
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        if (user.isEmpty())
        {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);

        }
        userService.deleteUser(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("users/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") String id,@RequestBody User entity){
        Optional<User> user = userService.getUserById(Long.valueOf(id));
        if (user.isEmpty())
        {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);

        }
        userService.updateUser(user.get().getId(),entity);
        return new ResponseEntity<>(entity,HttpStatus.OK);
    }








}
