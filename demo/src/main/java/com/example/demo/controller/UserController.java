package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity< Users> register(@RequestBody Users user){
        return  ResponseEntity.status(201).body(userService.register(user));
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return  ResponseEntity.status(200).body(userService.verify(user));
    }
}
