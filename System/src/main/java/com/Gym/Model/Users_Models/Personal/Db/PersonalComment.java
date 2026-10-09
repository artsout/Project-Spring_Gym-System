package com.Gym.Model.Users_Models.Personal.Db;


import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "personal_comment",

uniqueConstraints = {
@UniqueConstraint(
        name = "uk_user_personal_comment", // Nome da regra no banco (opcional)
        columnNames = {"user_id", "personal_id"} // Nomes EXATOS das colunas no banco
)
    })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id")
    private Personal personal;
}

