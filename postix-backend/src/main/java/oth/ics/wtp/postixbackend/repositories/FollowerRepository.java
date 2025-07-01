package oth.ics.wtp.postixbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import oth.ics.wtp.postixbackend.entities.Follower;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, String> {
    boolean existsByFollower_UsernameAndFollowing_Username(String followerUsername, String followingUsername);

    List<Follower> findByFollower_Username(String username);
    List<Follower> findByFollowing_Username(String username);

    Optional<Follower> findByFollower_UsernameAndFollowing_Username(String followerUsername, String followingUsername);
}
