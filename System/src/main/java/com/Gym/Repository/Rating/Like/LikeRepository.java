package com.Gym.Repository.Rating.Like;

import com.Gym.Model.Users_Models.Personal.Db.PersonalLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<PersonalLike,Long> {


}
