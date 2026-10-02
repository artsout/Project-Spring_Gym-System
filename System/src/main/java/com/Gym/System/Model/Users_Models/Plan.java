package com.Gym.System.Model.Users_Models;


import com.Gym.System.Model.Enum.UserPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserPlan userPlan;

    private Double price;

    private Double discount;

    @OneToMany(mappedBy = "userPlan",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<User> usersThatHaveThisPlan =new HashSet<>();

}
