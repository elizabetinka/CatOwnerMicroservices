package com.elizabetinka.lab4.catmicroservice2;

import com.elizabetinka.lab4.dto.CatDto;
import com.elizabetinka.lab4.jpa.Cat;


import java.util.ArrayList;
import java.util.List;

public class MapperToDto {

    public static CatDto toCatDto(Cat cat){
        if (cat == null) return null;
        Long id = null;
        if (cat.getOwner() != null){
            id = cat.getOwner().getId();
        }
        List<Long> ans = null;
        if (cat.getFriends() != null) {
            ans = new ArrayList<>();
            for (Cat cats: cat.getFriends()) {
                ans.add(cats.getId());
            }
        }
        com.elizabetinka.lab4.dto.Color col = null;
        if (cat.getColor() != null) {
            col=com.elizabetinka.lab4.dto.Color.valueOf(cat.getColor().toString());
        }

        return new CatDto(cat.getId(), cat.getName(), cat.getBirthday(), cat.getBreed(), col,id,ans);
    }
    public static List<CatDto> toCatsDto(List<Cat> cats){
        if (cats == null) return null;
        List<CatDto> catDtos = new ArrayList<>();
        for (Cat cat : cats){
            catDtos.add(toCatDto(cat));
        }
        return catDtos;
    }

}
