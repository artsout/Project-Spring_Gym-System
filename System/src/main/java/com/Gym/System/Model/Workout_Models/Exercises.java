package com.Gym.System.Model.Workout_Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exercises",indexes = {
        @Index(name = "idx_exercises_muscular_group", columnList = "muscularGroup")

})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String muscularGroup;
    @Column
    private String auxiliarMuscularGroup;
    @Column
    private String exampleGifPath;



    @OneToMany(mappedBy = "exercises",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<WorkoutExercises> workoutWithThisExercises = new HashSet<>();

}
