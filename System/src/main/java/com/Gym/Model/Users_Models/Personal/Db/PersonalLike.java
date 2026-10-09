package com.Gym.Model.Users_Models.Personal.Db;


import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;


@Entity
@Table(name = "personal_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id")
    private Personal personal;
}
