package com.elizabetinka.lab4.catmicroservice2;

import com.elizabetinka.lab4.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.dto.CatDto;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MapperFromDto {

    private CatRepository catRepository;
    private OwnerRepository ownerRepository;

    @Autowired
    public MapperFromDto(CatRepository catService,OwnerRepository ownerService) {
        this.catRepository = catService;
        this.ownerRepository = ownerService;

    }

    public  Cat toCat(CatDto catDto){
        Owner owner=null;
        if (catDto.getOwner_id() != null){
            //owner=ownerRepository.getById(catDto.getOwner_id());
            Optional<Owner> owner2 = ownerRepository.findById(catDto.getOwner_id());
            if (owner2.isPresent()){
                owner=owner2.get();
            }
            owner=ownerRepository.getReferenceById(catDto.getOwner_id());
        }

        Set<Cat> catSet = null;
        if (catDto.getFriends() != null) {
            catSet = new HashSet<>();
            for (Long id : catDto.getFriends()) {
                Cat cat = catRepository.getReferenceById(id);
                catSet.add(cat);
            }
        }
        com.elizabetinka.lab4.jpa.Color col = null;
        if (catDto.getColor() != null) {
            col=com.elizabetinka.lab4.jpa.Color.valueOf(catDto.getColor().toString());
        }
        Cat cat =  new Cat(catDto.getName(),catDto.getBirthday(), catDto.getBreed(),col,owner,catSet);
        return cat;
    }

}
