package com.chatApp.ChatApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates no-args constructor
@AllArgsConstructor // Generates all-args constructor
@Table(name = "Users")  // Change the table name to "app_user" to avoid the reserved word "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    //email field is unique for a specific user :
    @Column(unique = true, nullable = false)
    private String email;



    //phoneNumber field is unique for a specific user :
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    private String password;
}
