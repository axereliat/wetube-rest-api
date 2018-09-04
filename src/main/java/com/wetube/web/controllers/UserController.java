package com.wetube.web.controllers;

import com.wetube.common.constants.WebConstants;
import com.wetube.domain.models.UserRegisterBindingModel;
import com.wetube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = WebConstants.ALLOWED_PORT)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = "application/json")
    public Map<String, String> register(@RequestBody UserRegisterBindingModel bindingModel) {
        Map<String, String> map = new HashMap<>();

        map.put("error", "none");

        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            map.put("error", "Passwords do not match");

            return map;
        }
        if (this.userService.findByEmail(bindingModel.getEmail()) != null) {
            map.put("error", "Email is already taken.");
            return map;
        }
        if (this.userService.findByUsername(bindingModel.getUsername()) != null) {
            map.put("error", "Username is already taken.");
            return map;
        }

        this.userService.register(bindingModel);

        return map;
    }
}
