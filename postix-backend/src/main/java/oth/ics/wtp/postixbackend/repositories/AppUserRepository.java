package oth.ics.wtp.postixbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.postixbackend.entities.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
