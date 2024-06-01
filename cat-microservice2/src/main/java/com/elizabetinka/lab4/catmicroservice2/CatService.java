package com.elizabetinka.lab4.catmicroservice2;

import com.elizabetinka.lab4.jpa.Color;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.dto.CatDto;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CatService {

    public CatDto addCat(CatDto catDto);

    public CatDto getCat(@NonNull Long id);

    public List<CatDto> getAllCats();

    public boolean deleteById(@NonNull Long id);

    public boolean deleteAll();

    public void updateCat(CatDto cat) throws Exception ;

    public List<CatDto> getAllByName(String name);

    public List<CatDto> getAllByBirthday(LocalDate birthday);

    public List<CatDto> getAllByBreed(String breed);

    public List<CatDto> getAllByColor(Color color);

    public List<CatDto> getAllByOwnerId(Long id);

}
