package com.elizabetinka.lab4.jpa;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Cat implements Serializable {
    @Id
    /*
    @SequenceGenerator(
            name = "cat_sequence",
            sequenceName = "cat_sequence",
            allocationSize = 1
    )
    */

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private LocalDate birthday;

    @Column
    private String breed;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;


    //@ManyToOne(cascade= {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @ManyToOne(fetch=FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn (name="owner_id",nullable = true)
    @ToString.Exclude
    private Owner owner;


    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST},fetch = FetchType.EAGER)
    Set<Cat> friends = new HashSet<>();
/*
    @Override
    public int hashCode() {
        return Objects.hash(id,name,birthday,breed,color);
    }


    @Override
    public String toString() {
        return "cat";
    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat temp = (Cat) o;
        return Objects.equals(id, temp.id) && Objects.equals(name, temp.name) && Objects.equals(breed, temp.breed) && Objects.equals(birthday,temp.birthday) && Objects.equals(color,temp.color);
    }


    public Cat(String name, LocalDate birthday, String breed, Color color, Owner owner) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
    }

    public Cat(String name, LocalDate birthday, String breed, Color color, Owner owner, Set<Cat> friends) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        this.friends = friends;
    }
}
