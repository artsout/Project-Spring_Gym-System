package com.Gym.Model.Users_Models.Personal;


import com.Gym.Model.Enum.TypeOfRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "personal_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalRole {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeOfRole typeOfRole;

    public PersonalRole(TypeOfRole typeOfRole) {
        this.typeOfRole=typeOfRole;
    }
}
