package com.example.demo.n_controller;

import java.util.*;

import com.example.demo.n_model.model_user;
import com.example.demo.n_rep.rep_user;
import com.example.demo.n_table.user;
import com.google.gson.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/")
public class user_controller {

    @Autowired
    private rep_user rep_user;
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @PostMapping(path = "register")
    public Map<String, String> register(@RequestBody String message) {
        log.info("/register");
        
        Gson gson = new Gson();
        BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
        model_user body = gson.fromJson(message, model_user.class);
        Map<String, String> resp = new HashMap<>();

        try {
            user newUser = new user(body);
            newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));

            rep_user.save(newUser);
            
            resp.put("Status", "Success");
            resp.put("Detail", "Register Successfully");
            return resp;
        } catch (Exception e) {
            resp.put("Status", "Failed");
            resp.put("Detail", e.getMessage());
            return resp;
        }
    }

    @PostMapping(path = "login")
    public Map<String, String> login(@RequestBody String message) {
        log.info("/login");
        
        Gson gson = new Gson();
        model_user body = gson.fromJson(message, model_user.class);
        Map<String, String> resp = new HashMap<>();

        try {
            user user = rep_user.findByUsername(body.getUsername());

            boolean result = passwordEncoder().matches(body.getPassword(), user.getPassword());

            if (result == false) {
                resp.put("Status", "Failed");
                resp.put("Detail", "Incorrect Password");
                return resp;
            }
            
            resp.put("Status", "Success");
            resp.put("Detail", "Login Successfully");
            return resp;
        } catch (Exception e) {
            resp.put("Status", "Failed");
            resp.put("Detail", e.getMessage());
            return resp;
        }
    }
}