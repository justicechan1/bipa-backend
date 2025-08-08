package com.bipa.backend.service;

import com.bipa.backend.dto.TourPlaceDto;
import com.bipa.backend.entity.TourPlace;
import com.bipa.backend.repository.TourPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TourApiService {

    private final TourPlaceRepository tourPlaceRepository;

    private final String SERVICE_KEY = "tG94B7iG8/5EXJEmYtc3suI35UvEn/xLCzERZuMjiBMeM3L8P2mJMz893gvJMSaNTglRwXQFVCTA2VkJrgTFbw==";
    private final String BASE_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";

    private final Map<Integer, String> typeNameMap = Map.of(
            12, "관광지",
            14, "문화시설",
            15, "행사/공연/축제",
            25, "여행코스",
            28, "레포츠"
    );

    public List<TourPlaceDto> fetchPlaces() {
        List<TourPlaceDto> result = new ArrayList<>();
        int page = 1;

        try {
            while (true) {
                String uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                        .queryParam("serviceKey", SERVICE_KEY)
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "MyApp")
                        .queryParam("_type", "json")
                        .queryParam("areaCode", "38")
                        .queryParam("sigunguCode", "8")
                        .queryParam("numOfRows", "100")
                        .queryParam("pageNo", page)
                        .build(false)
                        .toUriString();

                RestTemplate restTemplate = new RestTemplate();
                String response = restTemplate.getForObject(uri, String.class);

                if (response != null && response.trim().startsWith("<")) {
                    System.out.println("❌ XML 응답 감지");
                    break;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode items = objectMapper.readTree(response)
                        .path("response").path("body").path("items").path("item");

                if (!items.isArray() || items.size() == 0) break;

                for (JsonNode item : items) {
                    int contentTypeId = item.path("contenttypeid").asInt();
                    if (!typeNameMap.containsKey(contentTypeId)) continue;

                    TourPlaceDto dto = new TourPlaceDto();
                    dto.setPlace_name(item.path("title").asText());
                    dto.setCategory(typeNameMap.get(contentTypeId));
                    dto.setAddress(item.path("addr1").asText());
                    dto.setX_cord(item.path("mapx").asText());
                    dto.setY_cord(item.path("mapy").asText());
                    dto.setDivision("관광지");

                    result.add(dto);
                }

                page++;
                Thread.sleep(300);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("✅ 총 가져온 관광지 개수: " + result.size());
        return result;
    }

    public int saveToDatabase() {
        List<TourPlaceDto> places = fetchPlaces();
        int addedCount = 0;

        for (TourPlaceDto dto : places) {
            String name = dto.getPlace_name().trim();

            if (tourPlaceRepository.findByPlaceName(name).isPresent()) {
                continue;
            }

            TourPlace entity = new TourPlace();
            entity.setPlaceName(name);
            entity.setCategory(dto.getCategory());
            entity.setAddress(dto.getAddress().trim());
            entity.setXCord(Double.parseDouble(dto.getX_cord()));
            entity.setYCord(Double.parseDouble(dto.getY_cord()));
            entity.setDivision(dto.getDivision());

            tourPlaceRepository.save(entity);
            addedCount++;
        }

        System.out.println("🟢 DB에 저장된 장소 수: " + addedCount);
        return addedCount;
    }
}
