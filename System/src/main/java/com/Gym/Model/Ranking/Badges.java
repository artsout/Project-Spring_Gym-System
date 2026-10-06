package com.Gym.Model.Ranking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "badges")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Badges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String photoUrl;

    private Integer rarity;//entre 0 a 5 de raridade

    @OneToMany(mappedBy = "badges",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<UserBadges> userBadges = new HashSet<>();
}
