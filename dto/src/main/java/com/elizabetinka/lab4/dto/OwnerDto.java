package com.elizabetinka.lab4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {com.elizabetinka.lab4.dao.model.Owner}
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@NoArgsConstructor(force = true)
@Data
public class OwnerDto implements Serializable {
    Long id;
    String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthday;
    List<Long> cats=new ArrayList<>();
    boolean isNull=true;

    public OwnerDto(Long id, String name, LocalDate birthday,List<Long> cats) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        if (cats != null) {
            this.cats.addAll(cats);
        }
        isNull=false;
    }
    public OwnerDto(boolean isNull) {
        this.isNull = isNull;
    }

}
