package by.university.demo.service;

import by.university.demo.dao.UserDao;
import by.university.demo.entity.Role;
import by.university.demo.entity.User;
import by.university.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository USER_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    private final UserDao USER_DAO;

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

    public List<User> findAll() {
        List<User> users;
        users = USER_DAO.findAll();
        return users;
    }
}
