package com.elizabetinka.lab4.ownermicroservice2;

import com.elizabetinka.lab4.dto.CatDto;
import com.elizabetinka.lab4.jpa.Cat;
import com.elizabetinka.lab4.jpa.Owner;
import com.elizabetinka.lab4.dto.OwnerDto;

import java.util.ArrayList;
import java.util.List;

public class MapperToDto {
    public static OwnerDto toOwnerDto(Owner owner){
        if (owner == null) return null;
        List<Long> ans=null;
        if (owner.getId() != null) {
            ans = new ArrayList<>();
            for (Cat i : owner.getCats())
                ans.add(i.getId());
        }
        return new OwnerDto(owner.getId(), owner.getName(), owner.getBirthday(),ans);
    }

    public static List<OwnerDto> toOwnerDto(List<Owner> owners){
        if (owners == null) return null;
        List<OwnerDto> ownerDtos = new ArrayList<>();
        for (Owner owner : owners){
            ownerDtos.add(toOwnerDto(owner));
        }
        return ownerDtos;
    }
}
