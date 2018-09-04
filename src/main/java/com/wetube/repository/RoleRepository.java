package com.wetube.repository;

import com.wetube.domain.entities.Role;
import com.wetube.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByAuthority(String authority);
}
