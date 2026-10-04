package com.Gym.System.Model.Users_Models;


import com.Gym.System.Model.Workout_Models.PersonalizedWorkout;
import com.Gym.System.Model.Workout_Models.ReadyWorkout;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "made_workout",indexes = {
        @Index(name = "idx_made_workout_user", columnList = "user_id"),
        @Index(name = "idx_made_workout_user_date", columnList = "user_id, workout_made_date"),
        @Index(name = "idx_made_workout_ready", columnList = "ready_workout_id"),
        @Index(name = "idx_made_workout_personalized", columnList = "personalized_workout_id")

})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MadeWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "workout_made_date")
    private LocalDateTime workoutMadeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ready_workout_id")
    private ReadyWorkout readyWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized_workout_id")
    private PersonalizedWorkout personalizedWorkout;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



}
