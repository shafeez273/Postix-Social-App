package oth.ics.wtp.postixbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.postixbackend.entities.Like;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {
    boolean existsByPostIdAndUserUsername(long postId, String userName);
    Optional<Like> findByPostId(long postId);
}
