package com.bipa.backend.repository;

import com.bipa.backend.entity.RestaurantPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RestaurantPlaceRepository extends JpaRepository<RestaurantPlace, Long> {
    Optional<RestaurantPlace> findByPlaceName(String placeName);
    Optional<RestaurantPlace> findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(String placeName);
    List<RestaurantPlace> findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(
            String q1, String q2, String q3
    );
}
