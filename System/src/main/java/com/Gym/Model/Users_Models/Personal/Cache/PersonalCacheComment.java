package com.Gym.Model.Users_Models.Personal.Cache;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash(value = "personal_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCacheComment {

    @Id
    private String id;

    @Indexed
    private String userId;


    private String ratingComment;

    @Indexed
    private String personalId;
}
