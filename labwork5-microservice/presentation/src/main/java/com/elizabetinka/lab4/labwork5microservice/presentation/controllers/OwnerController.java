package com.elizabetinka.lab4.labwork5microservice.presentation.controllers;

import com.elizabetinka.lab4.dto.*;
import com.elizabetinka.lab4.labwork5microservice.services.services.OwnerService;
import com.elizabetinka.lab4.labwork5microservice.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "api/v1/owner")
public class OwnerController {

    private final OwnerService ownerService;
    private final UserService userService;


    @Autowired
    public OwnerController(final OwnerService ownerService,final UserService userService) {
        this.ownerService = ownerService;
        this.userService = userService;
        try {
            System.out.println("vse sbs0");
            ownerService.addUser(new OwnerDto(null,"admin",null,null),"admin","admin","ROLE_ADMIN");
            System.out.println("vse sbs1");
            ownerService.addUser(new OwnerDto(null,"first_people",null,null),"user","user","ROLE_USER");
            System.out.println("vse sbs2");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }



    @GetMapping("/get")
    public List<OwnerDto> GetCats(@AuthenticationPrincipal UserDetails currentUser){
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (Objects.equals(role, "ROLE_ADMIN")){
            return ownerService.getAllOwners();
        }
        return Collections.singletonList(ownerService.getOwner(owner_id));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<OwnerDto> GetOwner(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser){
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (!Objects.equals(role, "ROLE_ADMIN")){
            if (!Objects.equals(owner_id, id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        OwnerDto ans = ownerService.getOwner(id);
        if (ans == null || ans.isNull()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ans);
    }


    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteCat (@PathVariable Long id,@AuthenticationPrincipal UserDetails currentUser) {
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (!Objects.equals(role, "ROLE_ADMIN")){
            if (!Objects.equals(owner_id, id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        if (ownerService.deleteById(id)){
            return ResponseEntity.ok("Delete");
        };
        return ResponseEntity.notFound().build();

    }



    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteAll (@AuthenticationPrincipal UserDetails currentUser) {
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (!Objects.equals(role, "ROLE_ADMIN")){
            if (ownerService.deleteById(owner_id)){
                return ResponseEntity.ok("Delete");
            };
            return ResponseEntity.notFound().build();
        }
        if (ownerService.deleteAll()){
            return ResponseEntity.ok("Delete");
        };
        return ResponseEntity.notFound().build();
    }



    @ExceptionHandler({ IllegalArgumentException.class })
    @GetMapping (path = {"/getby"})
    public ResponseEntity<List<OwnerDto>> GetOwnerBy(@RequestBody ForChooseOwner forChooseOwner,@AuthenticationPrincipal UserDetails currentUser){
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (!Objects.equals(role, "ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        Set<OwnerDto> ans = new HashSet<>();

        if (forChooseOwner.getName() != null){
            ans.addAll(ownerService.getAllByName(forChooseOwner.getName()));
        }
        if (forChooseOwner.getBirthday() != null){
            ans.addAll(ownerService.getAllByBirthday(forChooseOwner.getBirthday()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ans.stream().toList());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = {"/add/{login}/{password}"})
    public ResponseEntity<OwnerDto> AddOwner(@RequestBody OwnerDto ownerDto,@PathVariable String login,@PathVariable String password,@AuthenticationPrincipal UserDetails currentUser){
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());

        if (!Objects.equals(role, "ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        try {
            OwnerDto owner = ownerService.addUser(ownerDto,login,password,"ROLE_USER");
            return ResponseEntity.status(HttpStatus.OK).body(owner);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

}

