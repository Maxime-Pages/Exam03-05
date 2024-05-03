package maxime.pages.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import maxime.pages.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User save(User entity);
    Optional<User> findById(Long Id);
    Optional<User> findByEmail(String email);
    void deleteById(Long id);

    
}
