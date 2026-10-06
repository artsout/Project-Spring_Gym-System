package com.Gym.Model.Workout_Models;

import com.Gym.Model.Users_Models.MadeWorkout;
import com.Gym.Model.Users_Models.Payment;
import com.Gym.Model.Schedule_Models.PersonalizedSchedule;
import com.Gym.Model.Users_Models.Personal.Personal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personalized_workout",indexes = {
    @Index(name = "idx_personalized_workout_payment",columnList = "payment"),
        @Index(name = "idx_personalized_workout_personal",columnList = "personal"),
        @Index(name = "idx_personalized_workout_creationDate",columnList = "workoutCreationDate"),
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
    @JoinColumn(name = "personalized_workout_payment")
    private Payment payment;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized_workout_personal")
    private Personal personal;

    @OneToMany(mappedBy = "personalizedWorkout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<PersonalizedSchedule> schedulePersonalizedWorkout = new HashSet<>();

    @OneToMany(mappedBy = "personalizedWorkout" , cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutDays> personalizedWorkoutDays = new HashSet<>();


    @OneToMany(mappedBy = "personalizedWorkout",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<MadeWorkout> userPersonalizedMadeWorkout = new HashSet<>();
}
