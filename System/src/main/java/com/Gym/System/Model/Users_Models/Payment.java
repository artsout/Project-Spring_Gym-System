package com.Gym.System.Model.Users_Models;


import com.Gym.System.Model.Enum.PaymentType;
import com.Gym.System.Model.Workout_Models.PersonalizedWorkout;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payment",indexes = {
    @Index(name = "idx-user",columnList = "user"),
        @Index(name = "idx-payment-type",columnList = "paymentType"),
        @Index(name = "idx-payment-date",columnList = "paymentCreationDate")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentCreationDate;


    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user-payment")
    private User user;

    @OneToMany(mappedBy = "payment",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<PersonalizedWorkout> personalizedWorkoutPayments= new HashSet<>();

}
