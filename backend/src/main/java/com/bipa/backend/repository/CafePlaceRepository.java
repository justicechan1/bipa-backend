package com.bipa.backend.repository;

import com.bipa.backend.entity.CafePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CafePlaceRepository extends JpaRepository<CafePlace, Long> {
    Optional<CafePlace> findByPlaceName(String placeName);
}
