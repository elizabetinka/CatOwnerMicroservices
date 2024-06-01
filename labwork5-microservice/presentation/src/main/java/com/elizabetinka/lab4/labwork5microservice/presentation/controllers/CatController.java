package com.elizabetinka.lab4.labwork5microservice.presentation.controllers;


import com.elizabetinka.lab4.dto.*;
import com.elizabetinka.lab4.labwork5microservice.services.services.CatService;
import com.elizabetinka.lab4.labwork5microservice.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/cat")
public class CatController {

    private final CatService catService;
    private final UserService userService;


    @Autowired
    public CatController(final CatService catService,final UserService userService) {
        this.catService = catService; this.userService = userService;
    }

    @GetMapping("/get")
    public List<CatDto> GetCats(@AuthenticationPrincipal UserDetails currentUser){
        System.out.println("ya zdes");
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        if (Objects.equals(role, "ROLE_ADMIN")){
            return catService.getAllCats();
        }
        return catService.getAllCats().stream().filter(catDto -> (Objects.equals(catDto.getOwner_id(), userService.GetOwnerIdByUsername(currentUser.getUsername())))).toList();
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<CatDto> GetCat(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser){
        CatDto ans = catService.getCat(id);
        if (ans == null || ans.isNull()){
            return ResponseEntity.notFound().build();
        }
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());

        if (Objects.equals(role, "ROLE_ADMIN") || Objects.equals(ans.getOwner_id(), owner_id)){
            return ResponseEntity.status(HttpStatus.OK).body(ans);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteCat (@PathVariable Long id,@AuthenticationPrincipal UserDetails currentUser) {
        System.out.println("/delete/{id}");

        CatDto ans = catService.getCat(id);
        if (ans==null || ans.isNull()){
            return ResponseEntity.notFound().build();
        }


        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (Objects.equals(role, "ROLE_ADMIN") || Objects.equals(ans.getOwner_id(), owner_id)){
            if (catService.deleteById(id)){
                return ResponseEntity.ok("Delete");
            };
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteAll (@AuthenticationPrincipal UserDetails currentUser) {
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        if (Objects.equals(role, "ROLE_ADMIN")){
            if (!catService.deleteAll()){
                return ResponseEntity.ok("Delete");
            };
            return ResponseEntity.notFound().build();
        }
        List<CatDto> cats = catService.getAllCats().stream().filter(catDto -> (Objects.equals(catDto.getOwner_id(), owner_id))).toList();
        for (CatDto catDto : cats){
            if (!catService.deleteById(catDto.getId())){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok("Delete");
    }


    @ExceptionHandler({ IllegalArgumentException.class })
    @GetMapping (path = {"/getby"})
    public List<CatDto> GetCatsBy(@AuthenticationPrincipal UserDetails currentUser,@RequestBody ForChooseCat forChooseCat){
        System.out.println("GetCatsBy");
            Set<CatDto> ans = new HashSet<>();

            if (forChooseCat.getColor() != null){
                    Color ecolor = Color.valueOf(forChooseCat.getColor());
                    ans.addAll(catService.getAllByColor(ecolor));
            }
            if (forChooseCat.getName() != null){
            ans.addAll(catService.getAllByName(forChooseCat.getName()));
            }
            if (forChooseCat.getBirthday() != null){
            ans.addAll(catService.getAllByBirthday(forChooseCat.getBirthday()));
            }
            if (forChooseCat.getBreed() != null){
            ans.addAll(catService.getAllByBreed(forChooseCat.getBreed()));
            }
            if (forChooseCat.getOwner_id()!= null){
            ans.addAll(catService.getAllByOwnerId(forChooseCat.getOwner_id()));
            }
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
            if (Objects.equals(role, "ROLE_ADMIN")){
                return ans.stream().toList();
            }
            return ans.stream().filter(catDto -> (Objects.equals(catDto.getOwner_id(), owner_id))).toList();
    }


    @PostMapping(path = {"/add"})
    public ResponseEntity<CatDto> AddCat(@AuthenticationPrincipal UserDetails currentUser,@RequestBody CatDto catDto){
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());

        if (Objects.equals(role, "ROLE_ADMIN")){
            return ResponseEntity.status(HttpStatus.OK).body(catService.addCat(catDto));
        }
        if (!Objects.equals(catDto.getOwner_id(),owner_id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        return ResponseEntity.status(HttpStatus.OK).body(catService.addCat(catDto));
    }


    @PutMapping(path = {"/update"})
    public ResponseEntity<String> UpdateCat(@RequestBody CatDto catDto,@AuthenticationPrincipal UserDetails currentUser) throws Exception {
        String role=currentUser.getAuthorities().stream().toList().get(0).getAuthority();
        Long owner_id =userService.GetOwnerIdByUsername(currentUser.getUsername());
        CatDto ans = catService.getCat(catDto.getId());
        if (ans == null || ans.isNull()){
            return ResponseEntity.notFound().build();
        }
        if (Objects.equals(role, "ROLE_ADMIN") || Objects.equals(ans.getOwner_id(), owner_id)){
            try {
                catService.updateCat(catDto);
                return ResponseEntity.ok("Update");
            }
            catch (Exception e){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
