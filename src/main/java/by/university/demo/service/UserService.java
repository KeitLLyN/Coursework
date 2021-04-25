package by.university.demo.service;

import by.university.demo.entity.Role;
import by.university.demo.entity.User;
import by.university.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    public UserService(UserRepository USER_REPOSITORY, PasswordEncoder passwordEncoder) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return USER_REPOSITORY.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDB = USER_REPOSITORY.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));

        USER_REPOSITORY.save(user);

        return true;
    }
}
