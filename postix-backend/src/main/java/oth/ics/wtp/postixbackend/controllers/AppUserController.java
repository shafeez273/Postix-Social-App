package oth.ics.wtp.postixbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.postixbackend.dtos.AppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.dtos.SearchUserDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.services.AppUserService;
import oth.ics.wtp.postixbackend.services.AuthService;

import java.util.List;

@RestController public class AppUserController {
    private final AuthService authService;
    private final AppUserService userService;

    public AppUserController(AuthService authService, AppUserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping(value="users/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SearchUserDto> searchUsers(HttpServletRequest request, @PathVariable("userName") String userName) {
        AppUser currentUser = authService.getAuthenticatedUser(request);
        return userService.searchUsers(currentUser.getUsername(), userName);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto createUser(@RequestBody CreateAppUserDto createAppUser) {
        return userService.create(createAppUser);
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "users/{userName}")
    public void deleteUser(HttpServletRequest request, @PathVariable("userName") String userName) {
        authService.getAuthenticatedUser(request);
        userService.delete(userName);
    }

    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value = "users/login")
    public AppUserDto logIn(HttpServletRequest request) {
        AppUser user = authService.logIn(request);
        return userService.get(user.getUsername());
    }

    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "users/logout")
    public void logOut(HttpServletRequest request) {
        authService.logOut(request);
    }


}
