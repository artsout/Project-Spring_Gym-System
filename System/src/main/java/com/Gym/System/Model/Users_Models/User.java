package com.Gym.System.Model.Users_Models;


import com.Gym.System.Model.Address.Address;
import com.Gym.System.Model.Ranking.UserBadges;
import com.Gym.System.Model.Schedule_Models.PersonalizedSchedule;
import com.Gym.System.Model.Schedule_Models.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user" , indexes = {
    @Index(name = "idx-completeName" ,columnList = "completeName"),
    @Index(name = "idx-address",columnList = "address"),
    @Index(name = "idx-user-plan",columnList = "userPlan"),
    @Index(name = "idx-user-schedule",columnList = "userSchedule")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String completeName;

    @Email
    private String email;

    @Column(unique = true,updatable = false)
    private String matricula;

    @Column(unique = true)
    private String password;

    private String perfilImageUrl;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime userCreationDate;

    @Embedded
    @Column
    private Address address;


    @ColumnDefault("0")
    private Integer yearScheduleCount;

    @ColumnDefault("0")
    private Integer workoutsDone;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user-plan")
    private Plan userPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-schedule")
    private Schedule userSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-personalized-schedule")
    private PersonalizedSchedule personalizedSchedule;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_progress")
    private Progress progress;


    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Payment> userPayment = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<MadeWorkout> userMadeWorkout = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<UserBadges> userBadges = new HashSet<>();
}
