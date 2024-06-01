package com.elizabetinka.lab4.ownermicroservice2;

import com.elizabetinka.lab4.dto.UserDto;
import com.elizabetinka.lab4.jpa.MyUser;
import com.elizabetinka.lab4.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository repository;

    public Long GetOwnerIdByUsername(String username) throws Exception {
        Optional<MyUser> user = repository.findByLogin(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw  new Exception(username + " not found");

    }

    public UserDto GetUserByUsername(String username){
        MyUser user = repository.findByLogin(username).orElse(null);
        if (user != null) {
            return new UserDto(user.getId(),user.getPassword(),user.getLogin(),user.getRoles(),user.getOwner().getId());
        }
        return null;
    }
}
