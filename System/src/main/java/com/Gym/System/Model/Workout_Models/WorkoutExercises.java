package com.Gym.System.Model.Workout_Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Duration;

@Entity
@Table(name ="", indexes = {

})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExercises {

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
    @JoinColumn(name = "workout-exercises-days")
    private WorkoutExercises workoutExercises;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "workout-exercises")
    private Exercises exercises;
}
