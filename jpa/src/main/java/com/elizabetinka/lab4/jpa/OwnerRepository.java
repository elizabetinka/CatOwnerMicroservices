package com.elizabetinka.lab4.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {
    public List<Owner> findAllByName(String name);
    public Optional<Owner> findByName(String name);
    public List<Owner> findAllByBirthday(LocalDate birthday);

}
