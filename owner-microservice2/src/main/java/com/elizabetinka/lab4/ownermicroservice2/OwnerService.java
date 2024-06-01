package com.elizabetinka.lab4.ownermicroservice2;

import com.elizabetinka.lab4.jpa.MyUser;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;

@Service
public interface OwnerService {
    public OwnerDto createOwner(@NonNull String name, LocalDate birthday, List<Long> cats_id) throws Exception;

    public void updateOwner(OwnerDto owner) throws Exception;

    public boolean deleteById(@NonNull Long id);

    public OwnerDto getOwner(@NonNull Long id);


    public List<OwnerDto> getAllOwners();

    public OwnerDto addOwner(OwnerDto ownerDto);

    public boolean deleteAll();

    public List<OwnerDto> getAllByName(String name);

    public List<OwnerDto> getAllByBirthday(LocalDate birthday);

    public OwnerDto addUser(OwnerDto ownerDto, String login, String password,String role);
}
