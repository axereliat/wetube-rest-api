package com.wetube.service;

import com.wetube.domain.entities.User;
import com.wetube.domain.models.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(User user);

    void register(UserRegisterBindingModel bindingModel);

    User findById(Integer id);

    User findByEmail(String email);

    List<User> findAll();

    User findByUsername(String username);
}
