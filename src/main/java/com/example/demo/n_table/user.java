package com.example.demo.n_table;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import com.example.demo.n_model.model_user;

import lombok.*;

@Entity
@Getter
@Setter

public class user {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @SequenceGenerator(name="client_generator", sequenceName="client_sequence", allocationSize=1)
    @Column(name = "id", length = 256, updatable = false, nullable = false)
    private String id;

    @Column(length = 256, unique = true, nullable = false)
    private String username;
    @Column(name = "password",length = 256, unique = true, nullable = false)
    private String password;

    public user() {
        
    }

    public user(model_user body) {
        this.setUsername(body.getUsername());
        this.setPassword(body.getPassword());
    }
}