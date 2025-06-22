package oth.ics.wtp.postixbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.postixbackend.dtos.CreateFollowerDto;
import oth.ics.wtp.postixbackend.dtos.FollowerDto;
import oth.ics.wtp.postixbackend.services.FollowerService;

@RestController public class FollowerController {
    private final FollowerService followerService;

    @Autowired public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping(value = "users/{userName}/followers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FollowerDto createFollower(@PathVariable("userName") String userName, @RequestBody CreateFollowerDto createFollower) {
        return followerService.followUser(userName, createFollower);
    }

    @DeleteMapping(value = "users/{userName}/followers/{followerName}")
    public void removeFollower(@PathVariable("userName") String userName, @PathVariable("followerName") String followerName) {
        followerService.unfollowUser(userName, followerName);
    }

}
