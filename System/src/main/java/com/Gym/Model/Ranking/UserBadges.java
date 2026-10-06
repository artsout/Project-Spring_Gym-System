package com.Gym.Model.Ranking;


import com.Gym.Model.Users_Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_badges",indexes = {
        @Index(name = "idx_user_badges_badge_id", columnList = "bages_id"),
},
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_user_badges_user_badge", columnNames  = {"user_id", "bages_id"})
    })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBadges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "bages_id")
    private Badges badges;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
