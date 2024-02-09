package com.engsoft.linkederasmus.repository;

import com.engsoft.linkederasmus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByUsername(String username);

    Optional<User> findByIdUser(int idUser);

    List<User> findUsersByUsernameContainingIgnoreCase(String username);

    List<User> findAll();
}