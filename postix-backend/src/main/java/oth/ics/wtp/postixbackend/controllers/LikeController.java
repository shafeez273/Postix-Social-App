package oth.ics.wtp.postixbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @SecurityRequirement(name = "basicAuth")
    @PostMapping(value = "posts/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(HttpServletRequest request, @PathVariable("postId") long postId) {
        AppUser user = authService.getAuthenticatedUser(request);
        likeService.likePost(postId, user);
    }

    @SecurityRequirement(name = "basicAuth")
    @DeleteMapping("/posts/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(HttpServletRequest request, @PathVariable("postId") Long postId) {
        AppUser user = authService.getAuthenticatedUser(request);
        likeService.unlikePost(user.getUsername(), postId);
    }


    @GetMapping(value = "posts/{postId}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isLiked(HttpServletRequest request, @PathVariable("postId") long postId) {
        AppUser user = authService.getAuthenticatedUser(request);
        return likeService.isLiked(postId, user.getUsername());
    }
    @GetMapping("/posts/{postId}/likes/count")
    public int likeCount(@PathVariable("postId") long postId) {
        return likeService.numberOfLikes(postId);
    }

}
