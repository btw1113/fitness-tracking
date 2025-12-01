package com.example.fitness_tracking.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String[][] users = {
            {"admin", "$2a$10$ABCDE", "ROLE_ADMIN"},
            {"user", "$2a$10$FGHIJ", "ROLE_USER"},
            {"trainer", "$2a$10$KLMNO", "ROLE_TRAINER"}
    };

    public CustomUserDetailsService() {
        users[0][1] = passwordEncoder.encode("admin123");
        users[1][1] = passwordEncoder.encode("user123");
        users[2][1] = passwordEncoder.encode("trainer123");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (String[] user : users) {
            if (user[0].equals(username)) {
                return new User(
                        user[0],
                        user[1],
                        Collections.singletonList(new SimpleGrantedAuthority(user[2]))
                );
            }
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}