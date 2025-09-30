package com.bipa.backend.service;

import com.bipa.backend.entity.CafePlace;
import com.bipa.backend.entity.RestaurantPlace;
import com.bipa.backend.entity.UserBook;
import com.bipa.backend.repository.CafePlaceRepository;
import com.bipa.backend.repository.RestaurantPlaceRepository;
import com.bipa.backend.repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final CafePlaceRepository cafeRepo;
    private final RestaurantPlaceRepository restRepo;
    private final UserBookRepository userBookRepo;

    /**
     * 프론트에서 userId, placeName, division("cafe"|"restaurant")을 받아
     * 해당 테이블에서 place를 찾아 user_book(user_id, place_id, division)에 저장합니다.
     * 이미 존재하면 저장하지 않고 성공으로 처리합니다(멱등성).
     */
    @Transactional
    public SavedResult addByName(Long userId, String placeName, String divisionRaw) {
        String division = normalizeDivision(divisionRaw);
        if (!"cafe".equals(division) && !"restaurant".equals(division)) {
            throw new IllegalArgumentException("지원하지 않는 division: " + divisionRaw);
        }

        Long placeId;
        String resolvedName;

        if ("cafe".equals(division)) {
            CafePlace p = cafeRepo.findByPlaceName(placeName)
                    .orElseThrow(() -> new PlaceNotFound("카페 '" + placeName + "'를 찾을 수 없습니다."));
            placeId = p.getId();
            resolvedName = p.getPlaceName();
        } else {
            RestaurantPlace p = restRepo.findByPlaceName(placeName)
                    .orElseThrow(() -> new PlaceNotFound("음식점 '" + placeName + "'를 찾을 수 없습니다."));
            placeId = p.getId();
            resolvedName = p.getPlaceName();
        }

        boolean exists = userBookRepo.existsByUserIdAndDivisionAndPlaceId(userId, division, placeId);
        if (!exists) {
            UserBook book = new UserBook();
            book.setUserId(userId);
            book.setPlaceId(placeId);
            book.setDivision(division);
            userBookRepo.save(book);
        }

        return new SavedResult(resolvedName, division);
    }

    private String normalizeDivision(String d) {
        if (d == null) return "";
        d = d.trim().toLowerCase();

        // 한글 → 영어 변환
        if (d.equals("카페")) return "cafe";
        if (d.equals("음식점")) return "restaurant";

        // 영어 키워드 처리
        if (d.startsWith("cafe")) return "cafe";
        if (d.startsWith("rest")) return "restaurant";

        return d;
    }

    /** 컨트롤러 응답에 돌려줄 간단한 결과 DTO */
    public static class SavedResult {
        public final String placeName;
        public final String division;
        public SavedResult(String placeName, String division) {
            this.placeName = placeName;
            this.division = division;
        }
    }
    /** 찾는 장소가 없을 때 던지는 예외 */
    public static class PlaceNotFound extends RuntimeException {
        public PlaceNotFound(String msg) { super(msg); }
    }
}
