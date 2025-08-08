package com.bipa.backend.repository;

import com.bipa.backend.entity.TourPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourPlaceRepository extends JpaRepository<TourPlace, Long> {
    Optional<TourPlace> findByPlaceName(String placeName);
}
