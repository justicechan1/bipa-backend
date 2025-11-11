package com.bipa.backend.service;

import com.bipa.backend.dto.user.FirstLoginRequest;
import com.bipa.backend.dto.user.CharacterResponse;
import com.bipa.backend.entity.User;
import com.bipa.backend.entity.UserCharacter;
import com.bipa.backend.exception.NotFoundException;
import com.bipa.backend.repository.UserCharacterRepository;
import com.bipa.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCharacterRepository characterRepository;

    /**
     * 로그인 시 호출: 사용자 upsert + 캐릭터를 보장(없으면 생성)
     */
    @Transactional
    public Long firstLogin(FirstLoginRequest req) {
        var dto = req.getUser();

        final boolean[] created = { false };
        User user = userRepository.findById(dto.getId()).orElseGet(() -> {
            User u = new User();
            u.setId(dto.getId());
            u.setCreatedAt(LocalDateTime.now());
            // 최초 생성시에만 소셜 닉네임을 초기값으로 사용
            if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
                u.setNickname(dto.getNickname().trim());
            }
            created[0] = true;
            return u;
        });

        // ✅ 기존 유저는 닉네임을 절대 덮지 않음
        user.setConnectedAt(dto.getConnected_at());
        userRepository.save(user);

        ensureDefaultCharacter(user.getId());
        return user.getId();
    }

    /**
     * 사용자의 캐릭터가 없으면 기본 캐릭터 생성 후 반환
     */
    @Transactional
    public UserCharacter ensureDefaultCharacter(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        // 이미 캐릭터가 있으면 그대로 반환
        Optional<UserCharacter> existing = characterRepository.findByUser(user);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 기본 캐릭터 생성
        UserCharacter character = new UserCharacter();
        character.setUser(user);

        // 기본값(엔티티 컬럼이 NOT NULL이면 안전하게 0/1 등으로 초기화)
        character.setLevel(1);
        character.setExp(0);
        character.setMoney(0);
        character.setHungryGauge(0);
        character.setHeartGauge(0);
        character.setMaxActopus(0);
        character.setMaxFig(0);
        character.setMaxYudal(0);
        character.setMaxFish(0);
        character.setUpdatedAt(LocalDateTime.now());

        // ※ Place가 필수라면 여기서 기본 장소 세팅
        // character.setPlace(placeRepository.findById(1L).orElseThrow(...));

        return characterRepository.save(character);
    }

    /**
     * 닉네임 변경: 성공 시 true 반환, 없으면 NotFoundException
     */
    @Transactional
    public boolean changeNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
        user.setNickname(newNickname);
        userRepository.save(user);
        return true;
    }

    /**
     * 캐릭터 정보 조회
     *  - 캐릭터가 반드시 있어야 한다면 ensureDefaultCharacter(userId)로 대체 가능
     */
    @Transactional
    public CharacterResponse.Characters getCharacterByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        UserCharacter character = characterRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("CHARACTER_NOT_FOUND"));

        return new CharacterResponse.Characters(
                character.getId(),
                user.getId(),
                character.getLevel(),
                character.getExp(),
                character.getMoney(),
                character.getHungryGauge(),
                character.getHeartGauge(),
                character.getMaxActopus(),
                character.getMaxFig(),
                character.getMaxYudal(),
                character.getMaxFish()
        );
    }
}
