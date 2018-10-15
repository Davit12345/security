package com.notificationservice.security.repository;

import com.notificationservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User getByEmail(String email);
}
