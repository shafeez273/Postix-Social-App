package oth.ics.wtp.postixbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.postixbackend.entities.Follower;

import java.util.List;

public interface FollowerRepository extends CrudRepository<Follower, Long> {
    boolean existsByFollower_UsernameAndFollowing_Username(String followerUsername, String followingUsername);

    List<Follower> findByFollower_Username(String username);

    void deleteByFollower_UsernameAndFollowing_Username(String followerUsername, String followingUsername);
}
