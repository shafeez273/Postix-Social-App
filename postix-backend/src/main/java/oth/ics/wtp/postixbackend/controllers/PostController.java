package oth.ics.wtp.postixbackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.postixbackend.dtos.CreatePostDto;
import oth.ics.wtp.postixbackend.dtos.PostDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.services.AuthService;
import oth.ics.wtp.postixbackend.services.PostService;

import java.util.List;

@RestController public class PostController {
    private final PostService postService;
    private final AuthService authService;

    @Autowired public PostController(PostService postService, AuthService authService){
        this.postService = postService;
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "posts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto createPost(HttpServletRequest request, @RequestBody CreatePostDto createPostDto) {
        AppUser currentUser = authService.getAuthenticatedUser(request);
        return postService.create(currentUser.getUsername(), createPostDto);
    }

    @GetMapping(value = "users/me/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> listMyPosts(HttpServletRequest request) {
        AppUser user = authService.getAuthenticatedUser(request);
        return postService.listOwnPosts(user.getUsername());
    }

    @GetMapping(value = "users/{username}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> listUserPosts(@PathVariable String username, HttpServletRequest request) {
        AppUser user = authService.getAuthenticatedUser(request);
        return postService.listTheirPosts(user.getUsername(), username);
    }

    @GetMapping(value = "/timeline", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> listAllPosts(HttpServletRequest request) {
        AppUser user = authService.getAuthenticatedUser(request);
        return postService.listAllPosts(user.getUsername());
    }

    @DeleteMapping("posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(HttpServletRequest request, @PathVariable long postId) {
        AppUser currentUser = authService.getAuthenticatedUser(request);
        postService.delete(postId, currentUser.getUsername());
    }

}
