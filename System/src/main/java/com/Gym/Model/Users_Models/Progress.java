package com.Gym.Model.Users_Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "progress",indexes = {
        @Index(name = "idx_user_progress", columnList = "user_progress"),
        @Index(name = "idx_user_weight_height", columnList = "user_progress, weight, height"),
        @Index(name = "idx_weight", columnList = "weight"),
        @Index(name = "idx_height", columnList = "height"),
        @Index(name = "idx_body_fat", columnList = "body_fat_percentage")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Double weight;

    private Double height;

    private String diseases;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;


    @OneToMany(mappedBy = "progress",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<User> userProgresses = new HashSet<>();



}
