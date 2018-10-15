package com.notificationservice.security.repository;


import com.notificationservice.security.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Integer> {

    Authority getByName(String name);
}
