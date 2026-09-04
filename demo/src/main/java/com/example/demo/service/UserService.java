package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
     private AuthenticationManager authenticationManager;

    @Autowired
     private JwtService jwtService;

    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return  userRepo.save(user);
    }

    public String verify(Users user) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUserName(),
                                user.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUserName());
        }

        throw new RuntimeException("fail to login");
    }
}
