package com.Gym.System.Model.Schedule_Models;

import com.Gym.System.Model.Workout_Models.ReadyWorkout;
import com.Gym.System.Model.Users_Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personalized-schedule",indexes = {
        @Index(name = "idx-scheduleCreationDate", columnList = "scheduleCreationDate"),
        @Index(name = "idx-scheduleExpirationDate", columnList = "scheduleExpirationDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalizedSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime scheduleCreationDate;

    @Column
    private LocalDateTime scheduleExpirationDate;

    @Column(nullable = false)
    private Integer quantOfWorkouts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ready-workou-personalized-schedule")
    private ReadyWorkout readyWorkout;


    @OneToMany(mappedBy = "personalized-schedule",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<User> userThatUsesThisSchedule = new HashSet<>();
}
