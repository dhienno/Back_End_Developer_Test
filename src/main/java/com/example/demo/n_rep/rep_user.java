package com.example.demo.n_rep;
import com.example.demo.n_table.user;

import org.springframework.data.jpa.repository.JpaRepository;

/*
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
*/
public interface rep_user extends JpaRepository<user,String> {
    user findByUsername(String username);
}