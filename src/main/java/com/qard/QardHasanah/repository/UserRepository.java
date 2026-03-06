package com.qard.QardHasanah.repository;

import com.qard.QardHasanah.entity.Role;
import com.qard.QardHasanah.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String verificationToken);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    Long countByRole(Role role);
}
