package com.elizabetinka.lab4.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CatRepository extends JpaRepository<Cat,Long> {
    public List<Cat> findAllByOwnerId(Long id);
    public List<Cat> findAllByName(String name);
    public List<Cat> findAllByBirthday(LocalDate birthday);
    public List<Cat> findAllByBreed(String breed);
    public List<Cat> findAllByColor(Color color);
}
