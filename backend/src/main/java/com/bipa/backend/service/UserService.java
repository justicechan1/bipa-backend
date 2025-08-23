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
     * 최초 로그인 처리: 없으면 생성, 있으면 해당 id 반환
     */
    @Transactional
    public Long firstLogin(FirstLoginRequest req) {
        FirstLoginRequest.UserDto dto = req.getUser();

        // 이미 존재하면 그대로 반환
        Optional<User> existing = userRepository.findById(dto.getId());
        if (existing.isPresent()) {
            return existing.get().getId();
        }

        // 새 사용자 생성
        User user = new User();
        user.setId(dto.getId());
        user.setNickname(dto.getNickname());
        user.setConnectedAt(dto.getConnected_at());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        // 기본 캐릭터 생성
        UserCharacter character = new UserCharacter();
        character.setUser(user);
        // 기본값(레벨/경험치/머니/게이지 등)이 null이면 엔티티에서 @Column(nullable=false) + default 0 권장
        character.setUpdatedAt(LocalDateTime.now());
        characterRepository.save(character);

        return user.getId();
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
