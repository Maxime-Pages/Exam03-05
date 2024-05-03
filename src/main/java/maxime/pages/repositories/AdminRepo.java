package maxime.pages.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import maxime.pages.models.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin save(Admin entity);
    Optional<Admin> findById(long id);
    Optional<Admin> findByEmail(String email);
    
}
