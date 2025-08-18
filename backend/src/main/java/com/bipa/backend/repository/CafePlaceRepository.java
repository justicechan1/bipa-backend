package com.bipa.backend.repository;

import com.bipa.backend.entity.CafePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CafePlaceRepository extends JpaRepository<CafePlace, Long> {
    Optional<CafePlace> findByPlaceName(String placeName);
    Optional<CafePlace> findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(String placeName);
    List<CafePlace> findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(
            String q1, String q2, String q3
    );
}
