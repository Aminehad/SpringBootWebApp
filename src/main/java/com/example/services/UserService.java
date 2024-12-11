package com.example.services;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    /**
     * Save the user with a hashed password.
     *
     * @param user The user to be saved.
     * @return The saved user.
     */
    public AppUser saveUser(AppUser user) {
        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the user to the database
        return userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            var user1 = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user1.getEmail())
                    .password(user1.getPassword())
                    .build();
        }else{
            throw new UsernameNotFoundException(String.format("%s User not found", email));
        }
    }
}