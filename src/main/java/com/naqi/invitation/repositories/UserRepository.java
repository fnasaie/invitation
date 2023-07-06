package com.naqi.invitation.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naqi.invitation.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Optional<User> findByRole(@Param("role") String role);
}
