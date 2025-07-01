package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.dtos.FollowerDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.entities.Follower;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;
import oth.ics.wtp.postixbackend.repositories.FollowerRepository;

import java.util.List;

@Service public class FollowerService {
    private final FollowerRepository followerRepo;
    private final AppUserRepository userRepo;

    @Autowired public FollowerService(FollowerRepository followerRepo, AppUserRepository userRepo) {
        this.followerRepo = followerRepo;
        this.userRepo = userRepo;
    }

    public FollowerDto followUser(String followingUsername, String followerUsername) {
        AppUser follower = userRepo.findByUsername(followerUsername).orElseThrow(() -> ClientErrors.userNotFound(followerUsername));
        AppUser following = userRepo.findByUsername(followingUsername).orElseThrow(() -> ClientErrors.userNotFound(followingUsername));

        if (follower.getUsername().equals(following.getUsername())) {
            throw ClientErrors.cannotFollowYourself();
        }
        if (followerRepo.existsByFollower_UsernameAndFollowing_Username(follower.getUsername(), following.getUsername())) {
            throw ClientErrors.alreadyFollowing(followingUsername);
        }
        Follower entity = toEntity(follower, following);
        followerRepo.save(entity);
        return toDto(entity);
    }

    public void unfollowUser(String followingUserName, String followerUserName) {
        Follower relation = followerRepo.findByFollower_UsernameAndFollowing_Username(
                followerUserName, followingUserName
        ).orElseThrow(() -> ClientErrors.alreadyNotFollowing(followingUserName));

        followerRepo.delete(relation);
    }

    public List<FollowerDto> getFollowersOf(String username) {
        if (!userRepo.existsByUsername(username)) {
            throw ClientErrors.userNotFound(username);
        }

        return followerRepo.findByFollowing_Username(username)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<FollowerDto> getFollowedUsersOf(String username) {
        if (!userRepo.existsByUsername(username)) {
            throw ClientErrors.userNotFound(username);
        }

        return followerRepo.findByFollower_Username(username)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public boolean isFollowing(String followerUsername, String targetUsername) {
        return followerRepo.existsByFollower_UsernameAndFollowing_Username(followerUsername, targetUsername);
    }
    private FollowerDto toDto(Follower follower) {
        return new FollowerDto(follower.getId(), follower.getFollowing().getUsername(), follower.getFollower().getUsername());
    }
    private Follower toEntity(AppUser follower, AppUser following) {
        return new Follower(follower, following);
    }
}
