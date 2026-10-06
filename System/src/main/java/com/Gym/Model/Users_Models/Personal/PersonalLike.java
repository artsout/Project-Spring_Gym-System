package com.Gym.Model.Users_Models.Personal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "personal_likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalLike {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String personalId;


}
