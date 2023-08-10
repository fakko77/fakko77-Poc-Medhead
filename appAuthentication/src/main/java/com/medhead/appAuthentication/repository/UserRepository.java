package com.medhead.appAuthentication.repository;

import com.medhead.appAuthentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    User getByUsername(String username);
    Boolean existsByUsername(String username);


    Boolean existsByEmail(String email);
}
