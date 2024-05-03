package maxime.pages.serviceimplem;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxime.pages.models.Admin;
import maxime.pages.repositories.AdminRepo;
import maxime.pages.services.AdminService;

@Service
public class AdminImplem implements AdminService{

    @Autowired
    AdminRepo adminRepo;
    
    @Override
    public Admin createAdmin(Admin entity) {
        return adminRepo.save(entity);
    }

    @Override
    public Optional<Admin> getAdmingByEmail(String email) {
        return adminRepo.findByEmail(email);
    }
    
}
