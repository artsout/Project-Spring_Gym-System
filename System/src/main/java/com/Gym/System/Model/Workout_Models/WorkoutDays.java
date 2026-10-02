package com.Gym.System.Model.Workout_Models;


import com.Gym.System.Model.Enum.WorkoutDayStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workout-days",indexes = {
    @Index(name = "idx-workout-day",columnList = "dayName"),
    @Index(name = "idx-workout-day-status",columnList = "workoutDayStatus"),
        @Index(name = "idx-ready-workout",columnList = "readyWorkout"),
        @Index(name = "idx-personalized-workout",columnList = "personalizedWorkout")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDays {

    @Enumerated(EnumType.STRING)
    private com.Gym.System.Model.Enum.WorkoutDays dayName;

    @Enumerated(EnumType.STRING)
    private WorkoutDayStatus workoutDayStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ready-workout-day")
    private ReadyWorkout readyWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized-workout-day")
    private PersonalizedWorkout personalizedWorkout;


    @OneToMany(mappedBy = "workout-days" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutExercises> workoutExercisesFromDay = new HashSet<>();


}
