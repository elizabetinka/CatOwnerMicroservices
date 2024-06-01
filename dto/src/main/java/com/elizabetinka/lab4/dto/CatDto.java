package com.elizabetinka.lab4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class CatDto implements Serializable {
    Long id;
    String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthday;
    String breed;
    Color color;

    boolean isNull;

    Long owner_id;
    List<Long> friends = new ArrayList<>();

    public CatDto(Long id, String name, LocalDate birthday, String breed, Color color, Long owner_id, List<Long> friends) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.owner_id = owner_id;
        if (friends != null) {
            this.friends.addAll(friends);
        }
        this.isNull=false;

    }


    public CatDto(boolean isNull) {
        this.isNull = isNull;
    }
}
