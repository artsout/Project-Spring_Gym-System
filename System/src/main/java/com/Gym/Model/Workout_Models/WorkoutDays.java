package com.Gym.Model.Workout_Models;


import com.Gym.Model.Enum.WorkoutDay;
import com.Gym.Model.Enum.WorkoutDayStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workout_days",indexes = {
    @Index(name = "idx_workout_day",columnList = "dayName"),
    @Index(name = "idx_workout_day_status",columnList = "workoutDayStatus"),
        @Index(name = "idx_ready_workout",columnList = "readyWorkout"),
        @Index(name = "idx_personalized_workout",columnList = "personalizedWorkout")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDays {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkoutDay dayName;

    @Enumerated(EnumType.STRING)
    private WorkoutDayStatus workoutDayStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ready_workout_day")
    private ReadyWorkout readyWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized_workout_day")
    private PersonalizedWorkout personalizedWorkout;


    @OneToMany(mappedBy = "workoutDays" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutExercises> workoutExercisesFromDay = new HashSet<>();


}
