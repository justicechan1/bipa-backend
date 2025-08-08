package com.bipa.backend.service;

import com.bipa.backend.dto.FirstLoginRequest;
import com.bipa.backend.entity.User;
import com.bipa.backend.entity.UserCharacter;
import com.bipa.backend.repository.UserCharacterRepository;
import com.bipa.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.bipa.backend.dto.CharacterResponse;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final UserCharacterRepository characterRepository;

    @Transactional
    //사용자 처음 로그인 시 캐릭터 정보 생성
    public Long createUserIfNotExists(FirstLoginRequest.UserDto dto) {
        // 이미 존재하는지 확인
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

        // 캐릭터도 함께 생성
        UserCharacter character = new UserCharacter();
        character.setUser(user);
        character.setUpdatedAt(LocalDateTime.now());
        characterRepository.save(character);

        return user.getId();
    }

    @Transactional
    //사용자 이름 변경
    public void changeNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        user.setNickname(newNickname);
        userRepository.save(user);
    }

    @Transactional
    //캐릭터 정보 전달
    public CharacterResponse.Characters getCharacterByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        UserCharacter character = characterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("CHARACTER_NOT_FOUND"));

        return new CharacterResponse.Characters(
                character.getId(),
                user.getId(),
                character.getLevel(),
                character.getExp(),
                character.getMoney(),
                character.getHungryGauge(),
                character.getMaxActopus(),
                character.getMaxFig(),
                character.getMaxYudal(),
                character.getMaxFish()
        );
    }

}
