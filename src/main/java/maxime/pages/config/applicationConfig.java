package maxime.pages.config;

import maxime.pages.models.Admin;
import maxime.pages.models.User;
import maxime.pages.repositories.AdminRepo;
import maxime.pages.repositories.UserRepo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class applicationConfig {
    @Autowired
    UserRepo repository;
    @Autowired
    AdminRepo adminRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            
            Optional<User> user = repository.findByEmail(email);
            if (user != null) {
                return user.get();
            }
            
            Optional<Admin> admin = adminRepo.findByEmail(email);
            if (admin != null) {
                return admin.get();
            }
            throw new UsernameNotFoundException("User not found with email: " + email);
            
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
