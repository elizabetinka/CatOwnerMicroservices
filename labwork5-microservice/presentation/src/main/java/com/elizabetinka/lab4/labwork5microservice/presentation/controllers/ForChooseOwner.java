package com.elizabetinka.lab4.labwork5microservice.presentation.controllers;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;

@Getter
@Setter
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ForChooseOwner {
    String name;
    LocalDate birthday;
}
