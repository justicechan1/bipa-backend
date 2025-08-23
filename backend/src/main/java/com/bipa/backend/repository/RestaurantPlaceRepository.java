package com.bipa.backend.repository;

import com.bipa.backend.entity.RestaurantPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import com.bipa.backend.dto.game.ShopRow;

public interface RestaurantPlaceRepository extends JpaRepository<RestaurantPlace, Long> {
    Optional<RestaurantPlace> findByPlaceName(String placeName);
    Optional<RestaurantPlace> findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(String placeName);
    List<RestaurantPlace> findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(
            String q1, String q2, String q3
    );
    @Query("""
        SELECT r.placeName  AS placeName,
               r.address    AS address,
               r.firstPrice AS firstPrice,
               r.firstMenu  AS firstMenu
        FROM RestaurantPlace r
        WHERE r.firstPrice IS NOT NULL AND r.firstPrice > 0
        ORDER BY r.firstPrice DESC, r.id ASC
    """)
    List<ShopRow> findShopRows();
}
