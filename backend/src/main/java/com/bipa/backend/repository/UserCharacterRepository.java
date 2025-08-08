package com.bipa.backend.repository;

import com.bipa.backend.entity.User;
import com.bipa.backend.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    Optional<UserCharacter> findByUser(User user);
}
