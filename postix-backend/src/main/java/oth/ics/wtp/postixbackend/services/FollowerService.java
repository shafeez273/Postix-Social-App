package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.dtos.CreateFollowerDto;
import oth.ics.wtp.postixbackend.dtos.FollowerDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.entities.Follower;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;
import oth.ics.wtp.postixbackend.repositories.FollowerRepository;
@Service public class FollowerService {
    private final FollowerRepository followerRepo;
    private final AppUserRepository userRepo;

    @Autowired public FollowerService(FollowerRepository followerRepo, AppUserRepository userRepo) {
        this.followerRepo = followerRepo;
        this.userRepo = userRepo;
    }

    public FollowerDto followUser(String username, CreateFollowerDto createFollower) {
        AppUser follower = userRepo.findByUsername(createFollower.followerName()).orElseThrow(() -> ClientErrors.userNotFound(username));
        AppUser following = userRepo.findByUsername(username).orElseThrow(() -> ClientErrors.userNotFound(username));

        if (follower.getUsername().equals(following.getUsername())) {
            throw ClientErrors.cannotFollowYourself();
        }
        if (followerRepo.existsByFollower_UsernameAndFollowing_Username(follower.getUsername(), following.getUsername())) {
            throw ClientErrors.alreadyFollowing(username);
        }
        Follower entity = toEntity(follower, following);
        followerRepo.save(entity);
        return toDto(entity);
    }

    public void unfollowUser(String followingUserName, String followerUserName) {
        if (!userRepo.existsByUsername(followerUserName)) {
            throw ClientErrors.userNotFound(followerUserName);
        }
        if (!userRepo.existsByUsername(followingUserName)) {
            throw ClientErrors.userNotFound(followingUserName);
        }
        if (!followerRepo.existsByFollower_UsernameAndFollowing_Username(followerUserName, followingUserName)) {
            throw ClientErrors.alreadyNotFollowing(followingUserName);
        }
        followerRepo.deleteByFollower_UsernameAndFollowing_Username(followerUserName, followingUserName);
    }
    private FollowerDto toDto(Follower follower) {
        return new FollowerDto(follower.getId(), follower.getFollower(), follower.getFollowing());
    }
    private Follower toEntity(AppUser follower, AppUser following) {
        return new Follower(follower, following);
    }
}
