package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.dtos.CreatePostDto;
import oth.ics.wtp.postixbackend.dtos.PostDto;
import oth.ics.wtp.postixbackend.entities.Follower;
import oth.ics.wtp.postixbackend.entities.Post;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;
import oth.ics.wtp.postixbackend.repositories.FollowerRepository;
import oth.ics.wtp.postixbackend.repositories.PostRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service public class PostService {
    private final PostRepository postRepo;
    private final AppUserRepository userRepo;
    private final FollowerRepository followerRepo;
    private final LikeService likeService;

    @Autowired public PostService(PostRepository postRepo, AppUserRepository userRepo, FollowerRepository followerRepo, LikeService likeService) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.followerRepo = followerRepo;
        this.likeService = likeService;
    }

    public List<PostDto> listOwnPosts(String username) {
        if (!userRepo.existsByUsername(username)) {
            throw ClientErrors.userNotFound(username);
        }

        return postRepo.findByUserUsername(username).stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .limit(20)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> listTheirPosts(String currentUsername, String followingUsername) {
        if (!userRepo.existsByUsername(currentUsername)) {
            throw ClientErrors.userNotFound(currentUsername);
        }

        if (!userRepo.existsByUsername(followingUsername)) {
            throw ClientErrors.userNotFound(followingUsername);
        }

        if (!followerRepo.existsByFollower_UsernameAndFollowing_Username(currentUsername, followingUsername)) {
            throw ClientErrors.notFollowing(followingUsername);
        }
        return postRepo.findByUserUsername(followingUsername).stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .limit(20)
                .map(this::toDto)
                .collect(Collectors.toList());
     }

     public List<PostDto> listAllPosts(String currentUsername) {
         if (!userRepo.existsByUsername(currentUsername)) {
             throw ClientErrors.userNotFound(currentUsername);
         }
         List<Follower> following = followerRepo.findByFollower_Username(currentUsername);
         List<String> followerUsernames = following.stream().map(f -> f.getFollowing().getUsername()).toList();

         if (followerUsernames.isEmpty()) {
             return Collections.emptyList();
         }

         List<Post> allPosts = followerUsernames.stream()
                 .flatMap(user -> postRepo.findByUserUsername(user).stream())
                 .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                 .limit(20)
                 .toList();

         return allPosts.stream().map(this::toDto).collect(Collectors.toList());
     }

    public PostDto create(CreatePostDto createPost) {
        Post post = toEntity(createPost);
        postRepo.save(post);
        return toDto(post);
    }

    public void delete(long postId) {
        if (postRepo.existsById(postId)) {
            throw ClientErrors.postNotFound();
        }
        postRepo.deleteById(postId);
    }

    private Post toEntity(CreatePostDto createPost) {
        return new Post(createPost.message(), Instant.now());
    }
    private PostDto toDto(Post post) {
        return new PostDto(post.getId(), post.getMessage(), post.getTimestamp(), likeService.numberOfLikes(post.getId()));
    }


}
