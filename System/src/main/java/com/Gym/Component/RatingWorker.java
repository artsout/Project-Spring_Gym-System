package com.Gym.Component;

import com.Gym.Service.PersonalService;
import com.Gym.Service.Rating.CommentService;
import com.Gym.Service.Rating.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingWorker {


    private final StringRedisTemplate redisTemplate;
    private final CommentService commentService;


    private final LikeService likeService;

    @Scheduled(fixedRate = 5000)
    public void thrownLikesDbWorker() {
        try {
            likeService.thrownLikesToDataBase();
        } catch (Exception e) {
            log.error("Error to thrown likes", e);
        }
    }


    @Scheduled(fixedRate = 300000)
    @Transactional
    public void syncCommentCountsToDatabase() {

        Set<String> keys = redisTemplate.keys("personal:*:comments");

        if (keys == null || keys.isEmpty()) return;


        List<Object> values = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String key : keys) {
                connection.stringCommands().get(key.getBytes());
            }
            return null;
        });


        int i = 0;
        for (String key : keys) {
            Object rawValue = values.get(i++);
            if (rawValue != null) {

                String uuidTexto = key.replace("personal:", "").replace(":comments", "");
                UUID personalId = UUID.fromString(uuidTexto);
                Long quantidade = Long.parseLong(rawValue.toString());


                commentService.updateCommentCount(personalId, quantidade);
            }
        }
    }
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void syncLikeCountsToDatabase() {

        Set<String> keys = redisTemplate.keys("personal:*:likes");

        if (keys == null || keys.isEmpty()) return;

        List<Object> values = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String key : keys) {
                connection.stringCommands().get(key.getBytes());
            }
            return null;
        });

        int i=0;
        for (String key:keys){
            Object rawValue = values.get(i++);
            if (rawValue != null) {

                String uuidTexto = key.replace("personal:", "").replace(":likes", "");
                UUID personalId = UUID.fromString(uuidTexto);
                Long quantidade = Long.parseLong(rawValue.toString());


                likeService.updateLikeCount(personalId, quantidade);
            }
        }
    }

}



