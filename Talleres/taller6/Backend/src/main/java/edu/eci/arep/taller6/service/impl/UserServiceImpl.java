package edu.eci.arep.taller6.service.impl;
import edu.eci.arep.taller6.model.User;
import edu.eci.arep.taller6.repository.UserRepository;
import edu.eci.arep.taller6.security.JwtUtil;
import edu.eci.arep.taller6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && encoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    @Override
    public String getProfileUser(String token) {
        return jwtUtil.validateTokenAndGetUser(token);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
