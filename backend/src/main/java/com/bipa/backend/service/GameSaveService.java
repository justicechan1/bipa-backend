// service/GameSaveService.java
package com.bipa.backend.service;

import com.bipa.backend.dto.game.SaveCharactersRequest.CharactersPayload;
import com.bipa.backend.entity.User;
import com.bipa.backend.entity.UserCharacter;
import com.bipa.backend.repository.UserCharacterRepository;
import com.bipa.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class GameSaveService {

    private final UserCharacterRepository userCharRepo;
    private final UserRepository userRepo;

    @Transactional
    public void save(CharactersPayload p) {
        // 1) 유저 존재 확인 (없으면 404 대신 자동 생성하고 싶다면 createUser())
        User user = userRepo.findById(p.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + p.getUserId()));

        // 2) 캐릭터 업서트(유저 기준)
        UserCharacter uc = userCharRepo.findByUser_Id(p.getUserId())
                .orElseGet(() -> {
                    UserCharacter n = new UserCharacter();
                    n.setUser(user);
                    return n;
                });

        // 3) 부분 업데이트 (null이면 기존값 유지)
        if (p.getLevel()       != null) uc.setLevel(Math.max(1, p.getLevel()));
        if (p.getExp()         != null) uc.setExp(Math.max(0, p.getExp()));
        if (p.getMoney()       != null) uc.setMoney(Math.max(0, p.getMoney()));
        if (p.getHungryGauge() != null) uc.setHungryGauge(clamp01(p.getHungryGauge()));
        if (p.getHeartGauge()  != null) uc.setHeartGauge(clamp01(p.getHeartGauge()));
        if (p.getMaxActopus()  != null) uc.setMaxActopus(Math.max(0, p.getMaxActopus()));
        if (p.getMaxFig()      != null) uc.setMaxFig(Math.max(0, p.getMaxFig()));
        if (p.getMaxYudal()    != null) uc.setMaxYudal(Math.max(0, p.getMaxYudal()));
        if (p.getMaxFish()     != null) uc.setMaxFish(Math.max(0, p.getMaxFish()));

        userCharRepo.save(uc);
    }

    private int clamp01(Integer v) {
        if (v == null) return 0;
        return Math.max(0, Math.min(100, v));
    }
}
