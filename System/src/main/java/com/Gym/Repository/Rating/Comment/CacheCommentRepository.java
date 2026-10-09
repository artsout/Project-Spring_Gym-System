package com.Gym.Repository.Rating.Comment;



import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



@Repository                                                                                 //precisa para findAll pageable no redis
public interface CacheCommentRepository extends CrudRepository<PersonalCacheComment, String>,PagingAndSortingRepository<PersonalCacheComment, String> {

}
