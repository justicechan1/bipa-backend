package com.bipa.backend.repository;

import com.bipa.backend.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findByUserIdAndDivision(Long userId, String division);
    boolean existsByUserIdAndDivisionAndPlaceId(Long userId, String division, Long placeId);
}
