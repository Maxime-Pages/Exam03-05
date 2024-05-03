package maxime.pages.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import maxime.pages.models.Admin;

@Service
public interface AdminService {
    Admin createAdmin(Admin entity);
    
    Optional<Admin> getAdmingByEmail(String email);
}
