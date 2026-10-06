package com.Gym.Model.Users_Models;

import com.Gym.Model.Enum.TypeOfRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeOfRole userRole;


    public UserRole(TypeOfRole typeOfRole) {
        this.userRole=typeOfRole;
    }
}
