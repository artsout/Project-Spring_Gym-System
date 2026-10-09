package com.Gym.Repository.Rating.Comment;

import com.Gym.Dto.Page.PageResponse;
import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;
import com.Gym.Model.Users_Models.Personal.Db.PersonalComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<PersonalComment, Long> {

}
