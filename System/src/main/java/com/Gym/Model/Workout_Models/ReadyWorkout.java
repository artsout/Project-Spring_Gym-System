package com.Gym.Model.Workout_Models;


import com.Gym.Model.Enum.WorkoutType;
import com.Gym.Model.Users_Models.MadeWorkout;
import com.Gym.Model.Schedule_Models.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ready_workout",indexes = {
    @Index(name = "idx_workout_name",columnList = "workoutType"),
    @Index(name = "idx_workout_date",columnList = "workoutCreationDate")
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


    @OneToMany(mappedBy = "readyWorkout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Schedule> scheduleReadyWorkout = new HashSet<>();

    @OneToMany(mappedBy = "readyWorkout" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutDays> readyWorkoutDays = new HashSet<>();


    @OneToMany(mappedBy = "readyWorkout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<MadeWorkout> userReadyMadeWorkout = new HashSet<>();
}
