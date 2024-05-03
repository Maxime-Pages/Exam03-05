package maxime.pages.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import maxime.pages.models.User;

@Service
public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Long valueOf);

    void deleteUser(Long id);

    User updateUser(User entity);

    Optional<User> getUserByEmail(String email);
    
}
