package com.elizabetinka.lab4.labwork5microservice.services.services;


import lombok.NonNull;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface OwnerService {

    public void updateOwner(OwnerDto owner) throws Exception;

    public boolean deleteById(@NonNull Long id);

    public OwnerDto getOwner(@NonNull Long id);

    public void notify(Object object);
    public List<OwnerDto> getAllOwners();

    public OwnerDto addOwner(OwnerDto ownerDto);

    public boolean deleteAll();

    public List<OwnerDto> getAllByName(String name);

    public List<OwnerDto> getAllByBirthday(LocalDate birthday);

    public OwnerDto addUser(OwnerDto ownerDto, String login, String password,String role) throws InterruptedException, ExecutionException;
}
