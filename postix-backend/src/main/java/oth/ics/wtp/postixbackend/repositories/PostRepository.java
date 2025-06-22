package oth.ics.wtp.postixbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.postixbackend.entities.Post;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, Long> {
    Optional<Post> findById(long id);
    Optional<Post> findByUserUsername(String username);
    boolean existsById(long id);
}
