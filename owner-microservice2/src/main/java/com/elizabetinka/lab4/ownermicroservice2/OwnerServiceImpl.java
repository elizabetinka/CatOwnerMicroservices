package com.elizabetinka.lab4.ownermicroservice2;

import com.elizabetinka.lab4.jpa.MyUser;
import com.elizabetinka.lab4.jpa.OwnerRepository;
import com.elizabetinka.lab4.jpa.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elizabetinka.lab4.jpa.Owner;
import com.elizabetinka.lab4.dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl  implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final MapperFromDto mapperFromDto;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository,MapperFromDto mapperFromDto,UserRepository userRepository) {
        this.ownerRepository = ownerRepository;
        this.mapperFromDto = mapperFromDto;
        this.userRepository=userRepository;
    }

    @Override
    public OwnerDto createOwner(@NonNull String name, LocalDate birthday, List<Long> cats_id) throws Exception {
        OwnerDto ownerToUpdate = new OwnerDto(null,name,birthday,cats_id);
        Owner owner = ownerRepository.save(mapperFromDto.toOwner(ownerToUpdate));
        return MapperToDto.toOwnerDto(owner);
    }

    @Override
    public void updateOwner(OwnerDto owner) throws Exception {
        Owner ownerToUpdate = mapperFromDto.toOwner(owner);
        ownerRepository.save(ownerToUpdate);
    }

    @Override
    public boolean deleteById(@NonNull Long id) {
        if (!ownerRepository.existsById(id)) {return false;}
        try {
            userRepository.deleteById(id);
            ownerRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public OwnerDto getOwner(@NonNull Long id) {
        try {
            Optional<Owner> owner = ownerRepository.findById(id);
            if (owner.isEmpty()){
                return new OwnerDto(true);
            }
            return MapperToDto.toOwnerDto(ownerRepository.getReferenceById(id));
        }
        catch (Exception e) {
            return null;
        }

    }

    @Override
    public List<OwnerDto> getAllOwners() {
        return MapperToDto.toOwnerDto(ownerRepository.findAll());
    }

    @Override
    public OwnerDto addOwner(OwnerDto ownerDto) {
        Optional<Owner> ow = ownerRepository.findByName(ownerDto.getName());
        if (ow.isPresent()) {
            System.out.println("checked null");
            return MapperToDto.toOwnerDto(ow.get());

        }
        Owner owner = mapperFromDto.toOwner(ownerDto);

        return MapperToDto.toOwnerDto(ownerRepository.save(owner));
    }

    @Override
    public boolean deleteAll() {
        try {
            ownerRepository.deleteAll();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OwnerDto> getAllByName(String name) {
        return MapperToDto.toOwnerDto(ownerRepository.findAllByName(name));
    }

    @Override
    public List<OwnerDto> getAllByBirthday(LocalDate birthday) {
        return MapperToDto.toOwnerDto(ownerRepository.findAllByBirthday(birthday));
    }

    @Override
    public OwnerDto addUser(OwnerDto ownerDto, String login, String password,String role) {

        OwnerDto owner = addOwner(ownerDto);
        System.out.println("owner added");
        MyUser user=new MyUser(owner.getId(), password,login,role,ownerRepository.getReferenceById(owner.getId()));
        Optional<MyUser> myUser = userRepository.findByLogin(login);
        if (myUser.isPresent()) {

            System.out.println("checked null");

            System.out.println(myUser.get());
            return owner;

        }
        System.out.println("checked");

        userRepository.save(user);
        System.out.println("user added");
        return owner;
    }

}
