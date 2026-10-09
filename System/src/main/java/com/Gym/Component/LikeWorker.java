package com.Gym.Component;

import com.Gym.Service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeWorker {


    private final RatingService ratingService;

    @Scheduled(fixedRate = 5000)
    public void executarWorker() {
        try {
            ratingService.thrownLikesToDataBase();
        } catch (Exception e) {
            log.error("Error to thrown likes", e);
        }
    }
}
