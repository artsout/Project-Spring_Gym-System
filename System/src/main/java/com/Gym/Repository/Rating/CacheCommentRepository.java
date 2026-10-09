package com.Gym.Repository.Rating;


import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheCommentRepository extends CrudRepository<PersonalCacheComment,Long> {
}
