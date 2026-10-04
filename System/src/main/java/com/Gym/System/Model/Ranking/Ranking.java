package com.Gym.System.Model.Ranking;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "ranking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ranking {

    @Id
    private String id;

    private String madeWorkoutId;

    @Indexed
    private String userId;
}
