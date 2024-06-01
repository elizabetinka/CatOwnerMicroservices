package com.elizabetinka.lab4.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ForAddUserDto implements Serializable {
    OwnerDto ownerDto;
    String login;
    String password;
    String role;
}
