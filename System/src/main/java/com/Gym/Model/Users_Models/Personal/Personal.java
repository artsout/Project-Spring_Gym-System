package com.Gym.Model.Users_Models.Personal;


import com.Gym.Model.Address.Address;
import com.Gym.Model.Users_Models.Personal.Db.PersonalComment;
import com.Gym.Model.Users_Models.Personal.Db.PersonalLike;
import com.Gym.Model.Workout_Models.PersonalizedWorkout;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "personal",indexes = {
        @Index(name = "idx_completeName" ,columnList = "completeName"),
        @Index(name = "idx_address",columnList = "address") ,
        @Index(name = "idx_specialization",columnList = "specialization") ,
        @Index(name = "idx_experienceYears",columnList = "experienceYears") ,
        @Index(name = "idx_personal_role",columnList = "roles") ,


})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String completeName;

    @Email
    private String email;

    @NotBlank
    @Column(name = "password", unique = true ,nullable = false)
    private String password;

    private String perfilImageUrl;

    @Embedded
    @Column
    private Address address;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime personalCreationDate;

    @ColumnDefault("0")
    private Integer personalizedWorkoutCreatedCount;

    @Column
    private String specialization;

    @Column(updatable = false, nullable = false, unique = true)
    private String cref;

    @Column
    private String bio;

    @Column
    private Integer experienceYears;

    @ColumnDefault("0")
    private Long personalCountLikes;

    @OneToMany(mappedBy = "personal", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<PersonalizedWorkout> workoutMadeByPersonal = new HashSet<>();

    @OneToMany(mappedBy = "personal", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<PersonalLike> personalLikes = new HashSet<>();

    @OneToMany(mappedBy = "personal", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<PersonalComment> personalComments = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name ="personal_role_join",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<PersonalRole> roles = new HashSet<>();


    public boolean isPasswordCorrect(@NotBlank @Size(min = 5,max = 20) String password, Argon2Password4jPasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password , this.password);
    }
}
