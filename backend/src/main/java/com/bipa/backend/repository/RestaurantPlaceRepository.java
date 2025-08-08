package com.bipa.backend.repository;

import com.bipa.backend.entity.RestaurantPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantPlaceRepository extends JpaRepository<RestaurantPlace, Long> {
    Optional<RestaurantPlace> findByPlaceName(String placeName);
}
