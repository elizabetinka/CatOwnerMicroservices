package com.elizabetinka.lab4.catmicroservice2;

import com.elizabetinka.lab4.jpa.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.elizabetinka.lab4.dto.CatDto;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CatServiceImpl implements CatService{


    private final CatRepository catRepository;
    private final MapperFromDto mapperFromDto;
    private final OwnerRepository ownerRepository;


    @Autowired
    public CatServiceImpl(CatRepository catRepository,MapperFromDto mapperFromDto,
                          OwnerRepository ownerRepository) {
        this.catRepository = catRepository;
        this.mapperFromDto = mapperFromDto;
        this.ownerRepository = ownerRepository;
    }


    @Override
    public CatDto addCat(CatDto catDto) {
        Cat cat = mapperFromDto.toCat(catDto);
        cat = catRepository.save(cat);
        return MapperToDto.toCatDto(cat);
    }

    @Override
    public CatDto getCat(@NonNull Long id) {
        try {
            Optional<Cat> cat = catRepository.findById(id);
            if (cat.isPresent()) {
                return MapperToDto.toCatDto(cat.get());
            }
            CatDto catDto = new CatDto(true);
            return catDto;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CatDto> getAllCats() {
        return MapperToDto.toCatsDto(catRepository.findAll());
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        if (!catRepository.existsById(id)) {return false;}
        try {
            catRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            catRepository.deleteAll();
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    @Override
    public void updateCat(CatDto cat) throws Exception {
        System.out.println(cat);
        Optional<Cat> cot = catRepository.findById(cat.getId());
        if (cot.isPresent()) {
            Cat catFromDb = cot.get();
            catFromDb.setName(cat.getName());
            catFromDb.setBreed(cat.getBreed());
            catFromDb.setBirthday(cat.getBirthday());
            com.elizabetinka.lab4.jpa.Color col = null;
            if (cat.getColor() != null) {
                col=com.elizabetinka.lab4.jpa.Color.valueOf(cat.getColor().toString());
            }
            catFromDb.setColor(col);
            catFromDb.setOwner(ownerRepository.getReferenceById(cat.getOwner_id()));
            catRepository.save(catFromDb);
        }
    }

    @Override
    public List<CatDto> getAllByName(String name) {
        List<Cat> cats = catRepository.findAllByName(name);
        return MapperToDto.toCatsDto(catRepository.findAllByName(name));
    }

    @Override
    public List<CatDto> getAllByBirthday(LocalDate birthday) {
        return MapperToDto.toCatsDto(catRepository.findAllByBirthday(birthday));
    }

    @Override
    public List<CatDto> getAllByBreed(String breed) {
        return MapperToDto.toCatsDto(catRepository.findAllByBreed(breed));
    }


    @Override
    public List<CatDto> getAllByColor(Color color) {
        return MapperToDto.toCatsDto(catRepository.findAllByColor(color));
    }

    @Override
    public List<CatDto> getAllByOwnerId(Long id) {
        return MapperToDto.toCatsDto(catRepository.findAllByOwnerId(id));
    }

}
