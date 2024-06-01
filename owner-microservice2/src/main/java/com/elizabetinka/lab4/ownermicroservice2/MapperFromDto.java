package com.elizabetinka.lab4.ownermicroservice2;

import com.elizabetinka.lab4.jpa.CatRepository;
import com.elizabetinka.lab4.jpa.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.jpa.Cat;
import com.elizabetinka.lab4.dto.OwnerDto;
import com.elizabetinka.lab4.jpa.Owner;

import java.util.HashSet;
import java.util.Set;

@Service
public class MapperFromDto {
    private OwnerRepository ownerRepository;
    private CatRepository catRepository;

    @Autowired
    public MapperFromDto(OwnerRepository ownerService,CatRepository catService) {
        this.ownerRepository = ownerService;
        this.catRepository = catService;
    }

    public Owner toOwner(OwnerDto ownerDto){
        Set<Cat> catSet = null;
        if (ownerDto.getCats() != null) {
            catSet = new HashSet<>();
            for (Long id : ownerDto.getCats()) {
                Cat cat = catRepository.getReferenceById(id);
                catSet.add(cat);
            }
        }
        return new Owner(ownerDto.getName(),ownerDto.getBirthday(),catSet);
    }
}
