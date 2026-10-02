package com.Gym.System.Model.Workout_Models;


import com.Gym.System.Model.Enum.WorkoutType;
import com.Gym.System.Model.Schedule_Models.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ready-workout",indexes = {
    @Index(name = "idx-workout-name",columnList = "workoutType"),
    @Index(name = "idx-workout-date",columnList = "workoutCreationDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadyWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkoutType workoutType;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime workoutCreationDate;

    @Column(nullable = false)
    private Integer quantityOfDays;//determina quant de dias


    @OneToMany(mappedBy = "ready-workout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Schedule> scheduleReadyWorkout = new HashSet<>();

    @OneToMany(mappedBy = "ready-workout" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutDays> readyWorkoutDays = new HashSet<>();
}
