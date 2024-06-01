package com.elizabetinka.lab4.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(force = true)
@Data
public class UserDto implements Serializable {
    Long id;
    String login;
    String password;
    Long owner_id;
    String roles;
    boolean isNull=true;

    public UserDto(Long id, String password, String login, String roles,Long owner_id) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.roles = roles;
        this.owner_id = owner_id;
        isNull=false;
    }
    public UserDto(Long id, String password, String login, Long ownerId, String roles) {
        this.id = id;
        this.password = password;
        this.login = login;
        owner_id = null;
        this.roles = roles;
        isNull=false;
    }
    public UserDto(boolean isNull) {
        this.isNull = isNull;
    }
}
