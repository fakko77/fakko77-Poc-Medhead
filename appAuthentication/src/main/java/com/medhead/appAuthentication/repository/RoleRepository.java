package com.medhead.appAuthentication.repository;

import com.medhead.appAuthentication.model.ERole;
import com.medhead.appAuthentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Role getById(Long aLong);
}
