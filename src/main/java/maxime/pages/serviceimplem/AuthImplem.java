package maxime.pages.serviceimplem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import maxime.pages.models.Admin;
import maxime.pages.models.User;
import maxime.pages.security.JwtService;
import maxime.pages.services.AdminService;
import maxime.pages.services.AuthService;
import maxime.pages.services.UserService;

@Service
public class AuthImplem implements AuthService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;


    @Override
    public String loginAdmin(Admin admin, String password) {
        if(bCryptPasswordEncoder.matches(password, admin.getPassword()))
            return jwtService.generateToken(admin);
        return null;
    }

    @Override
    public String loginUser(User user, String password) {
        if(bCryptPasswordEncoder.matches(password, user.getPassword()))
            return jwtService.generateToken(user);
        return null;
    }

    @Override
    public Admin registerAdmin(Admin entity) {
        String passwordEncoded = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoded);
        return adminService.createAdmin(entity);
    }

    @Override
    public User registerUser(User entity) {
        String passwordEncoded = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoded);
        return userService.createUser(entity);
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtService.extractUsername(token.substring("Bearer ".length()));
    }

    @Override
    public String encryptPW(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    
    
    
}
