// service/GaugeCalculator.java
package com.bipa.backend.service;

import org.springframework.stereotype.Component;

@Component
public class GaugeCalculator {
    public int calcHungry(int price) {
        if (price >= 15000) return 30;
        if (price >= 10000) return 22;
        if (price >=  7000) return 16;
        if (price >=  5000) return 12;
        if (price >=  3000) return 8;
        return 5;
    }
}

