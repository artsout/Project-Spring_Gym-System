package com.Gym.Controller;
//Comments + Like

import com.Gym.Dto.Comment.CommentRequestDto;
import com.Gym.Dto.Page.PageResponse;

import com.Gym.Model.Users_Models.Personal.Cache.PersonalCacheComment;
import com.Gym.Model.Users_Models.Personal.Db.PersonalComment;
import com.Gym.Model.Users_Models.Personal.Db.PersonalLike;
import com.Gym.Service.Rating.CommentService;
import com.Gym.Service.Rating.LikeService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class PersonalRatingController {


    //Isso tudo e redis
    //pegar todos os likes  e comments personal = Pageble
    //like algum personal ou comment sobre ele

    private final LikeService likeService;
    private final CommentService commentService;


    @PostMapping("/like/{personalId}")
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<Void> likePersonal(@AuthenticationPrincipal Jwt jwt ,@PathVariable UUID personalId){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        likeService.likePersonal(userId,personalId);//user n pode curtir 2 vezes
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/all-likes/{personalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<PersonalLike>> getAllLikesFromPersonal(@PathVariable UUID personalId,
                                                                              @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        PageResponse<PersonalLike> personalLikes = likeService.getAllLikesFromPersonal(personalId,pageable);
        return ResponseEntity.ok(personalLikes);
    }



   @PostMapping
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<Void> commentPersonal(@AuthenticationPrincipal Jwt jwt ,@PathVariable UUID personalId , @Valid @RequestBody CommentRequestDto commentRequestDto){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        commentService.commentPersonal(userId,personalId,commentRequestDto);//pode comentar 1 vez apenas
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/all-comments/{personalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<Object>> getAllCommentsFromPersonal(@PathVariable UUID personalId,
                                                                                         @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse<Object> personalComments = commentService.getAllCommentsFromPersonal(personalId,pageable);
        return ResponseEntity.ok(personalComments);
    }


    @PatchMapping("/edit/{commentId}")
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<PersonalComment> updateComment(@AuthenticationPrincipal Jwt jwt , @PathVariable Long commentId,@Valid @RequestBody CommentRequestDto commentRequestDto){
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));//tem q verificar se o comment e do user pra n poder editar qualquer um
        PersonalComment personalComment = commentService.updateComment(userId ,commentId , commentRequestDto);
        return ResponseEntity.ok(personalComment);

    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAuthority('USER_USER')")
    public ResponseEntity<Void> userDeleteComment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable  Long commentId) {

        UUID userId = UUID.fromString(jwt.getSubject());

        commentService.userDeleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/admin/{commentId}")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<Void> adminDeleteComment(
            @PathVariable Long commentId) {
        commentService.adminDeleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
