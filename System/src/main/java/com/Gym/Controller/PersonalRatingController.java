package com.Gym.Controller;
//Comments + Like

import com.Gym.Dto.Page.PageResponse;
import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheLike;
import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;
import com.Gym.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class PersonalRatingController {


    //Isso tudo e redis
    //pegar todos os likes  e comments personal = Pageble
    //like algum personal ou comment sobre ele

    private final RatingService ratingService;

    @PostMapping("/like/{personalId}")
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<Void> likePersonal(@AuthenticationPrincipal Jwt jwt ,@PathVariable UUID personalId){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        ratingService.likePersonal(userId,personalId);//user n pode curtir 2 vezes
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
 /*
    @PostMapping
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<Void> commentPersonal(@AuthenticationPrincipal Jwt jwt ,@PathVariable UUID personalId){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        ratingService.commentPersonal(userId,personalId);//pode comentar 1 vez apenas
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("/all-likes/{personalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<PersonalCacheLike>> getAllLikesFromPersonal(@PathVariable UUID personalId,
                                                                                   @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        PageResponse<PersonalCacheLike> personalLikes = ratingService.getAllLikesFromPersonal(personalId,pageable);
        return ResponseEntity.ok(personalLikes);
    }

    @GetMapping("/all-comments/{personalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<PersonalCacheComment>> getAllCommentsFromPersonal(@PathVariable UUID personalId,
                                                                                         @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse<PersonalCacheComment> personalComments = ratingService.getAllCommentsFromPersonal(personalId,pageable);
        return ResponseEntity.ok(personalComments);
    }


    @PatchMapping("/edit/{commentId}")
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<PersonalCacheComment> updateComment(@AuthenticationPrincipal Jwt jwt , @PathVariable Long commentId){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));//tem q verificar se o comment e do user pra n poder editar qualquer um
        PersonalCacheComment personalCacheComment = ratingService.updateComment(userId , commentId);
        return ResponseEntity.ok(personalCacheComment);

    }
*/
}
