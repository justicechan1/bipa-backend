package com.bipa.backend.repository;

import com.bipa.backend.entity.TourPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TourPlaceRepository extends JpaRepository<TourPlace, Long> {
    Optional<TourPlace> findByPlaceName(String placeName);
    List<TourPlace> findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(
            String q1, String q2, String q3
    );
}
