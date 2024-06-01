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
public class Owner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    LocalDate birthday;

    @OneToMany(mappedBy="owner",
            fetch=FetchType.EAGER,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH},orphanRemoval=true)
    Set<Cat> cats = new HashSet<>();
/*
    @Override
    public int hashCode() {
        return Objects.hash(id,name,birthday);
    }


    @Override
    public String toString() {
        return "owner";
    }
*/
    public Owner(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }
    public Owner(String name, LocalDate birthday,Set<Cat> cats) {
        this.name = name;
        this.birthday = birthday;
        this.cats = cats;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner temp = (Owner) o;
        return Objects.equals(id, temp.id) && Objects.equals(name, temp.name) && Objects.equals(birthday,temp.birthday) ;
    }



    public void setCats(Set<Cat> cats) throws Exception {
        if (cats == null){return;}
        for (Cat cat:cats) {
            if  (cat.getOwner() != null && !Objects.equals(cat.getOwner().getId(),this.id)){
                throw new Exception("cat have another owner");
            }
            else{
                cat.setOwner(this);
            }
        }
    }

    public void setUnsafetyCats(Set<Cat> cats) throws Exception {
        this.cats = cats;
    }

*/
}
