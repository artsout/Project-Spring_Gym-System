package com.Gym.Service.Rating;


import com.Gym.Dto.Comment.CommentRequestDto;
import com.Gym.Dto.Page.PageResponse;
import com.Gym.Exception.BusinessException;
import com.Gym.Exception.ObjectNotFound;
import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;
import com.Gym.Model.Users_Models.Personal.Db.PersonalComment;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.User;
import com.Gym.Repository.Rating.Comment.CacheCommentRepository;
import com.Gym.Repository.Rating.Comment.CommentRepository;
import com.Gym.Service.PersonalService;
import com.Gym.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.UUID;



@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CacheCommentRepository cacheCommentRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PersonalService personalService;
    private final RedisTemplate redisTemplate;

    private static final String COMMENT_COUNT_KEY_PATTERN = "personal:%s:comments";


    public PersonalComment findById(Long commentId){
        if(commentId==null ){
            throw new InvalidParameterException("Parameter cant be null");
        }
        return commentRepository.findById(commentId)
                .orElseThrow(()-> new ObjectNotFound("Object nor found"));
    }


    @Transactional
    public void commentPersonal(UUID userId , UUID personalId, CommentRequestDto commentRequestDto){
        if(userId==null || personalId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }

        try {
            User user = userService.findById(userId);
            Personal personal = personalService.findById(personalId);

            PersonalComment personalComment = new PersonalComment();
            personalComment.setPersonal(personal);
            personalComment.setUser(user);
            personalComment.setDescription(commentRequestDto.getDescription());
            personalComment.setCreatedDate(LocalDateTime.now());

            personalComment = commentRepository.save(personalComment);


            PersonalCacheComment personalCacheComment = new PersonalCacheComment(personalComment);

            cacheCommentRepository.save(personalCacheComment);


            String key = String.format(COMMENT_COUNT_KEY_PATTERN, personalId);

            redisTemplate.opsForValue().increment(key);


        }catch (DataIntegrityViolationException d){
            throw  new BusinessException("A user can just comment one time");
        }
    }


    @Transactional
    public void updateCommentCount(UUID personalId , Long quantity){
        personalService.updateCountComment(personalId , quantity);
    }


    public PageResponse<Object> getAllCommentsFromPersonal(UUID personalId, Pageable pageable){
        if(pageable==null || personalId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }

        PageResponse<PersonalCacheComment> redisComments = getAllRedis(personalId,pageable);


        if (redisComments.content() != null && !redisComments.content().isEmpty()) {
            return new PageResponse<>(
                    new ArrayList<>(redisComments.content()),
                    redisComments.pageSize(),
                    redisComments.pageNumber(),
                    redisComments.totalElements(),
                    redisComments.totalPages(),
                    redisComments.isLast()
            );
        }

        PageResponse<PersonalComment> dbComments = getAllDb(personalId,pageable);

        return new PageResponse<>(
                new ArrayList<>(dbComments.content()),
                dbComments.pageSize(),
                dbComments.pageNumber(),
                dbComments.totalElements(),
                dbComments.totalPages(),
                dbComments.isLast()
        );
    }

    private PageResponse<PersonalCacheComment> getAllRedis(UUID personalId,Pageable pageable){
        if(pageable==null || personalId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }
        Page<PersonalCacheComment> pageResult = cacheCommentRepository.findAll(pageable);
        return new PageResponse(
                pageResult.getContent(),
                pageResult.getPageable().getPageSize(),
                pageResult.getPageable().getPageNumber(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }
    private PageResponse<PersonalComment> getAllDb(UUID personalId, Pageable pageable){
        if(pageable==null || personalId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }
        Page<PersonalComment> pageResult = commentRepository.findAll(pageable);

        return new PageResponse(
                pageResult.getContent(),
                pageResult.getPageable().getPageSize(),
                pageResult.getPageable().getPageNumber(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

    public PersonalComment updateComment(UUID userId,Long commentId,CommentRequestDto commentRequestDto){
        if(userId==null || commentId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }
        PersonalComment personalComment = findById(commentId);

        if(!personalComment.getUser().getId().equals(userId)){
            throw  new BusinessException("You cant update somebody else comment");
        }

        personalComment.setDescription(commentRequestDto.getDescription());

        return  commentRepository.save(personalComment);
    }

    public void userDeleteComment(UUID userId,Long commentId){
        if(userId==null || commentId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }
        PersonalComment personalComment = findById(commentId);
        if(!personalComment.getUser().getId().equals(userId)){
            throw  new BusinessException("You cant delete somebody else comment");
        }

        commentRepository.delete(personalComment);
    }

    public void adminDeleteComment(Long commentId){
        if(commentId == null){
            throw new InvalidParameterException("Parameter cant be null");
        }
        PersonalComment personalComment = findById(commentId);

        commentRepository.delete(personalComment);
    }
}
