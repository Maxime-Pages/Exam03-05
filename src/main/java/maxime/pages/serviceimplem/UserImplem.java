package maxime.pages.serviceimplem;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxime.pages.models.User;
import maxime.pages.repositories.UserRepo;
import maxime.pages.services.UserService;

@Service
public class UserImplem implements UserService {
    @Autowired
    UserRepo userRepo;

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User updateUser(User entity) {
        return userRepo.save(entity);
    }
    
}
