package edu.eci.arep.taller6.controller;


import edu.eci.arep.taller6.model.User;
import edu.eci.arep.taller6.security.JwtUtil;
import edu.eci.arep.taller6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/arep")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User u = userService.loginUser(user.getEmail(), user.getPassword());
        if (u == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String token = jwtUtil.generateToken(u.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }


    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = userService.getProfileUser(token);
        if (email == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        User user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
