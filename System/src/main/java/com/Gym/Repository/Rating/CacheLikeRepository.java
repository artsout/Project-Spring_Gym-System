package com.Gym.Repository.Rating;

import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CacheLikeRepository extends CrudRepository<PersonalCacheLike,String> {
    Optional<PersonalCacheLike> findByUserIdAndPersonalId(String s, String s1);
}
