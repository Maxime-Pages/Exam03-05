package maxime.pages.services;

import org.springframework.stereotype.Service;

import maxime.pages.models.Admin;
import maxime.pages.models.User;

@Service
public interface AuthService {

    String loginAdmin(Admin admin, String password);
    Admin registerAdmin(Admin entity);


    String loginUser(User user, String password);
    User registerUser(User entity);

    String getEmailFromToken(String token);
    String encryptPW(String password);
}
