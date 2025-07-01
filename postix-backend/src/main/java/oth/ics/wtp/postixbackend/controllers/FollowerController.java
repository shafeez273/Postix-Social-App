package oth.ics.wtp.postixbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.postixbackend.dtos.FollowerDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.services.AuthService;
import oth.ics.wtp.postixbackend.services.FollowerService;

import java.util.List;

@RestController public class FollowerController {
    private final FollowerService followerService;
    private final AuthService authService;

    @Autowired public FollowerController(FollowerService followerService, AuthService authService) {
        this.followerService = followerService;
        this.authService = authService;

    }

    @PostMapping(value = "users/{followingUserName}/followers", produces = MediaType.APPLICATION_JSON_VALUE)
    public FollowerDto createFollower(HttpServletRequest request, @PathVariable("followingUserName") String followingUserName) {
        AppUser currentUser = authService.getAuthenticatedUser(request);
        return followerService.followUser(followingUserName, currentUser.getUsername());
    }

    @DeleteMapping(value = "users/{userName}/followers")
    public void removeFollower(HttpServletRequest request, @PathVariable("userName") String userName) {
        AppUser currentUser = authService.getAuthenticatedUser(request);
        followerService.unfollowUser(userName, currentUser.getUsername());
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/users/{username}/followers")
    public List<FollowerDto> getFollowers(@PathVariable String username) {
        return followerService.getFollowersOf(username);
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/users/{username}/following")
    public List<FollowerDto> getFollowing(@PathVariable String username) {
        return followerService.getFollowedUsersOf(username);
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/users/{username}/follow-status")
    public boolean isFollowing(@PathVariable String username,
                               @RequestParam("target") String target) {
        return followerService.isFollowing(username, target);
    }



}
