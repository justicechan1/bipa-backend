// service/GameShopService.java
package com.bipa.backend.service;

import com.bipa.backend.dto.game.ShopPlaceDto;
import com.bipa.backend.dto.game.ShopRow;
import com.bipa.backend.repository.CafePlaceRepository;
import com.bipa.backend.repository.RestaurantPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameShopService {

    private final CafePlaceRepository cafeRepo;
    private final RestaurantPlaceRepository restaurantRepo;
    private final GaugeCalculator gauge;

    public List<ShopPlaceDto> listByDivision(String divisionRaw) {
        String division = divisionRaw == null ? "" : divisionRaw.trim().toLowerCase();

        List<ShopRow> rows = switch (division) {
            case "cafe" -> cafeRepo.findShopRows();
            case "restaurant" -> restaurantRepo.findShopRows();
            default -> List.of(); // 필요시 예외로 바꿔도 됨
        };

        List<ShopPlaceDto> out = new ArrayList<>(rows.size());
        for (ShopRow r : rows) {
            Integer price = r.getFirstPrice();
            int hungry = (price == null || price <= 0) ? 0 : gauge.calcHungry(price);

            out.add(new ShopPlaceDto(
                    r.getPlaceName(),
                    r.getAddress(),
                    hungry,
                    price,
                    r.getFirstMenu()
            ));
        }
        return out;
    }
}
