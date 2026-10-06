package com.Gym.Model.Schedule_Models;


import com.Gym.Model.Workout_Models.ReadyWorkout;
import com.Gym.Model.Users_Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "schedule",indexes = {
        @Index(name = "idx_schedule_ready_wourkout", columnList = "readyWorkout"),
        @Index(name = "idx_scheduleCreationDate", columnList = "scheduleCreationDate"),
        @Index(name = "idx_scheduleExpirationDate", columnList = "scheduleExpirationDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

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
    @JoinColumn(name = "ready_workout_schedule")
    private ReadyWorkout readyWorkout;


    @OneToMany(mappedBy = "userSchedule",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<User> userThatUsesThisSchedule = new HashSet<>();
}
