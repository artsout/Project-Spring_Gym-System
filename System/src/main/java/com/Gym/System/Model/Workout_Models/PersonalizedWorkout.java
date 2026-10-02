package com.Gym.System.Model.Workout_Models;

import com.Gym.System.Model.Users_Models.Payment;
import com.Gym.System.Model.Schedule_Models.PersonalizedSchedule;
import com.Gym.System.Model.Users_Models.Personal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personalized-workout",indexes = {
    @Index(name = "idx-personalized-workout-payment",columnList = "payment"),
        @Index(name = "idx-personalized-workout-personal",columnList = "personal"),
        @Index(name = "idx-personalized-workout-creationDate",columnList = "workoutCreationDate"),
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalizedWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime workoutCreationDate;

    @Column(nullable = false)
    private Integer quantityOfDays;//determina quant de dias

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized-workout-payment")
    private Payment payment;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized-workout-personal")
    private Personal personal;

    @OneToMany(mappedBy = "personalizedWorkout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<PersonalizedSchedule> schedulePersonalizedWorkout = new HashSet<>();

    @OneToMany(mappedBy = "personalizedWorkout" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutDays> personalizedWorkoutDays = new HashSet<>();
}
