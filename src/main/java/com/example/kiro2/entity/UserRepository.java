package com.example.kiro2.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT id FROM User ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Integer findLastAddedUserId();

    //Object findByUserName(String userName);

    //Object findByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    //User findByName(String name);

   // User findByEmail(String email);

}
