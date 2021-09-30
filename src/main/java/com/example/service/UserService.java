package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user){
        User userDb = userRepo.findByUsername(user.getUsername());

        if(userDb != null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if(user.getEmail() != null && !user.getEmail().isEmpty()){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Creator. Please, visit next link: http://localhost:8080/activate/%s" +
                            " to activate your account",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Account activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null){
            return false;
        }

        user.setActive(true);
        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }
}
