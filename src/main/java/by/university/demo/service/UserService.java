package by.university.demo.service;

import by.university.demo.dao.UserDao;
import by.university.demo.entity.Role;
import by.university.demo.entity.User;
import by.university.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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

    public void generateUsers(){
        File file = new File("src/main/resources/username.txt");
        try(
                Scanner scanner = new Scanner (file);
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/db/migration/V6__GenerateUsers_DB.sql"));
        ){
            while (scanner.hasNextLine()){
                String username = scanner.nextLine();
                String email = String.format("%s.user@gmail.com", username);
                String decodedPassword = RandomStringUtils.randomAlphabetic(1,7);
                String password = PASSWORD_ENCODER.encode(decodedPassword);

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);

                String sql = USER_DAO.generate(user) + "\n";
                writer.append(sql);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
