package com.Gym.Model.Users_Models.Personal.Cache;


import com.Gym.Model.Users_Models.Personal.Db.PersonalComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;


@RedisHash(value = "personal_like", timeToLive = 604800)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCacheComment {

    @Id
    private String id;

    @Indexed
    private String userId;


    private String description;

    private String createdDate;

    @Indexed
    private String personalId;


    public PersonalCacheComment(PersonalComment personalComment){
        if (personalComment != null) {
            this.createdDate = personalComment.getCreatedDate() != null ? String.valueOf(personalComment.getCreatedDate()) : null;
            this.description = personalComment.getDescription();

            if (personalComment.getUser() != null) {
                this.userId = String.valueOf(personalComment.getUser().getId());
            }
            if (personalComment.getPersonal() != null) {
                this.personalId = String.valueOf(personalComment.getPersonal().getId());
            }
        }
    }
}
