package com.elizabetinka.lab4.jpa;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class MyUser implements Serializable {
    @Id
    private Long id;

    @Column(unique = true)
    String login;

    @Column
    String password;



    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @MapsId
    Owner owner;

    String roles;

    public MyUser( String password, String login, String roles) {
        this.password = password;
        this.login = login;
        this.roles = roles;
    }
    public MyUser( String password, String login, String roles,Owner owner) {
        this.password = password;
        this.login = login;
        this.roles = roles;
        this.owner = owner;
    }
    public MyUser(Long id, String password, String login, String roles,Owner owner) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.roles = roles;
        this.owner = owner;
    }
}
