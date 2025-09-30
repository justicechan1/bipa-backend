package com.bipa.backend.repository;

import com.bipa.backend.entity.CafePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.bipa.backend.dto.game.ShopRow;
import org.springframework.data.jpa.repository.*;

public interface CafePlaceRepository extends JpaRepository<CafePlace, Long> {
    Optional<CafePlace> findByPlaceName(String placeName);
    Optional<CafePlace> findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(String placeName);
    List<CafePlace> findByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(
            String q1, String q2, String q3
    );
    @Query("""
        SELECT c.placeName  AS placeName,
               c.address    AS address,
               c.firstPrice AS firstPrice,
               c.firstMenu  AS firstMenu
        FROM CafePlace c
        WHERE c.firstPrice IS NOT NULL AND c.firstPrice > 0
        ORDER BY c.firstPrice DESC, c.id ASC
    """)
    List<ShopRow> findShopRows();
}
