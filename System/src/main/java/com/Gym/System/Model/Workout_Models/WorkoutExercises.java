package com.Gym.System.Model.Workout_Models;


import com.Gym.System.Model.Enum.WorkoutDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Duration;

@Entity
@Table(name ="workout_exercises", indexes = {
        @Index(name = "idx_workout_exercises_days", columnList = "workout_exercises_days"),
        @Index(name = "idx_workout_exercises_id", columnList = "workout_exercises")

})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExercises {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("3")
    private Integer series;

    @ColumnDefault("12")
    private Integer repetitions;


    private Duration seriesInterval;

    @ColumnDefault("0")
    private float userCurrentWeight;

    private String userAnnotations;

    @ColumnDefault("false")
    private boolean done;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercises_days")
    private WorkoutDays workoutDays;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercises")
    private Exercises exercises;
}
