package oth.ics.wtp.postixbackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.services.AuthService;
import oth.ics.wtp.postixbackend.services.LikeService;

@RestController public class LikeController {
    private final AuthService authService;
    private final LikeService likeService;

    @Autowired public LikeController(LikeService likeService, AuthService authService) {
        this.likeService = likeService;
        this.authService = authService;
    }
    @PostMapping(value = "posts/{postId}/likes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setLike(HttpServletRequest request, @PathVariable("postId") long postId) {
        AppUser user = authService.getAuthenticatedUser(request);
        likeService.setLike(postId, user);
    }
}
