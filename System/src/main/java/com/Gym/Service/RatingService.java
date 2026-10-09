package com.Gym.Service;


import com.Gym.Exception.BusinessException;
import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheLike;
import com.Gym.Model.Users_Models.Personal.Db.PersonalLike;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.User;
import com.Gym.Repository.Rating.CacheCommentRepository;
import com.Gym.Repository.Rating.CacheLikeRepository;
import com.Gym.Repository.Rating.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.InvalidParameterException;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final CacheLikeRepository cacheLikeRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate redisTemplate;
    private final UserService userService;
    private final PersonalService personalService;

    private final CacheCommentRepository cacheCommentRepository;

    public  void likePersonal(UUID userId,UUID personalId){
        if (userId == null || personalId == null) {
            throw new InvalidParameterException("Parameters cannot be null");
        }

        log.info("Checking if the user already liked the personal");


        var existingLike = cacheLikeRepository.findByUserIdAndPersonalId(
                String.valueOf(userId), String.valueOf(personalId)
        );


        if (existingLike.isPresent()) {
            throw new BusinessException("A user can only like the same personal 1 time");
        }


        PersonalCacheLike personalLike = new PersonalCacheLike();
        personalLike.setId(userId + ":" + personalId); // Uma boa prática é compor o ID para evitar colisões
        personalLike.setUserId(String.valueOf(userId));
        personalLike.setPersonalId(String.valueOf(personalId));

        cacheLikeRepository.save(personalLike);
    }


    //pegar os likes e jogar no database
    //tambem vai fzr o count e mandar junto
    @Transactional
    public  void thrownLikesToDataBase(){
        log.info("Worker throwing to database.");

        ScanOptions options = ScanOptions.scanOptions()
                .match("PersonalLike:*")
                .count(200)
                .build();

        List<PersonalLike> likesToSaveInDb = new ArrayList<>();
        List<PersonalCacheLike> keysToDelete = new ArrayList<>();


        try (Cursor<String> cursor = redisTemplate.scan(options)){
            while (cursor.hasNext()){
                String key = cursor.next();


                String idDoCache = key.split(":")[1];

                Optional<PersonalCacheLike> cacheOpt = cacheLikeRepository.findById(idDoCache);

                if (cacheOpt.isPresent()) {
                    PersonalCacheLike cache = cacheOpt.get();

                    keysToDelete.add(cache);

                    PersonalLike dbLike = new PersonalLike();

                    User user = new User();
                    user.setId(UUID.fromString(cache.getUserId()));
                    dbLike.setUser(user);

                    Personal personal = new Personal();
                    personal.setId(UUID.fromString(cache.getPersonalId()));
                    dbLike.setPersonal(personal);

                    likesToSaveInDb.add(dbLike);
                }
                if (likesToSaveInDb.size() >= 200) {
                    persistirELimparLote(likesToSaveInDb, keysToDelete);
                }
            }
        }
        if (!likesToSaveInDb.isEmpty()) {
            persistirELimparLote(likesToSaveInDb, keysToDelete);
        }
    }
    private void persistirELimparLote(List<PersonalLike> dbLikes, List<PersonalCacheLike> redisLikes) {
        if (!dbLikes.isEmpty()) {
            likeRepository.saveAll(dbLikes);
        }
        cacheLikeRepository.deleteAll(redisLikes);

        dbLikes.clear();
        redisLikes.clear();
    }

}