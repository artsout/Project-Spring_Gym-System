package com.Gym.Service.Rating;


import com.Gym.Dto.Page.PageResponse;
import com.Gym.Exception.BusinessException;

import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheLike;
import com.Gym.Model.Users_Models.Personal.Db.PersonalLike;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.User;

import com.Gym.Repository.Rating.Comment.CacheCommentRepository;
import com.Gym.Repository.Rating.Like.CacheLikeRepository;
import com.Gym.Repository.Rating.Like.LikeRepository;
import com.Gym.Service.PersonalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.InvalidParameterException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class LikeService {

    private final CacheLikeRepository cacheLikeRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate redisTemplate;
    private final PersonalService personalService;



    private static final String LIKE_COUNT_KEY_PATTERN = "personal:%s:likes";


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

        //salvar a entidade like
        PersonalCacheLike personalLike = new PersonalCacheLike();
        personalLike.setId(userId + ":" + personalId); // Uma boa prática é compor o ID para evitar colisões
        personalLike.setUserId(String.valueOf(userId));
        personalLike.setPersonalId(String.valueOf(personalId));

        cacheLikeRepository.save(personalLike);

        //salvar o count de likes

        String key = String.format(LIKE_COUNT_KEY_PATTERN, personalId);

        redisTemplate.opsForValue().increment(key);

    }

    @Transactional
    public void updateLikeCount(UUID personalId , Long quantity){
        personalService.updateCountLikes(personalId , quantity);
    }

    //pegar os likes e jogar no database
    //tambem vai fzr o count e mandar junto
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
    @Transactional
    private void persistirELimparLote(List<PersonalLike> dbLikes, List<PersonalCacheLike> redisLikes) {
        if (!dbLikes.isEmpty()) {
            likeRepository.saveAll(dbLikes);
        }
        cacheLikeRepository.deleteAll(redisLikes);

        dbLikes.clear();
        redisLikes.clear();
    }


    public PageResponse<PersonalLike> getAllLikesFromPersonal(UUID personalId , Pageable pageable){
        if (pageable == null || personalId == null) {
            throw new InvalidParameterException("Parameters cannot be null");
        }
        Personal personal = personalService.findById(personalId);

        Page<PersonalLike> pageResult = likeRepository.findAll(pageable);

        return new PageResponse(
                pageResult.getContent(),
                pageResult.getPageable().getPageSize(),
                pageResult.getPageable().getPageNumber(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
                );
    }
}