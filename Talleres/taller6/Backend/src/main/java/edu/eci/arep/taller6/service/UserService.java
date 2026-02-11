package edu.eci.arep.taller6.service;

import edu.eci.arep.taller6.model.User;


public interface UserService {
    void saveUser(User user);
    User loginUser(String email, String password);
    String getProfileUser(String token);
    User findByEmail(String email);
}
