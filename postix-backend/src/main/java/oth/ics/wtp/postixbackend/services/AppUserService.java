package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.WeakCrypto;
import oth.ics.wtp.postixbackend.dtos.AppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.dtos.SearchUserDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;
import oth.ics.wtp.postixbackend.repositories.FollowerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service public class AppUserService {
    private final AppUserRepository appUserRepo;
    private final FollowerRepository followerRepo;

    @Autowired public AppUserService(AppUserRepository appUserRepo, FollowerRepository followerRepo) {
        this.appUserRepo = appUserRepo;
        this.followerRepo = followerRepo;
    }

    public List<SearchUserDto> searchUsers(String currentUsername, String query) {
        List<AppUser> searchResults = appUserRepo.findByUsernameContainingIgnoreCase(query);
        return searchResults.stream()
                .filter(user -> !user.getUsername().equals(currentUsername)) // exclude self
                .map(user -> {
                    boolean isFollowing = followerRepo.existsByFollower_UsernameAndFollowing_Username(
                            currentUsername, user.getUsername());
                    return new SearchUserDto(user.getUsername(), isFollowing);
                })
                .collect(Collectors.toList());
    }


    public AppUserDto create(CreateAppUserDto createAppUser) {
        if (createAppUser.name() == null || createAppUser.name().isEmpty() ||
            createAppUser.password() == null || createAppUser.password().isEmpty()) {
            throw ClientErrors.invalidCredentials();
        }
        if (appUserRepo.existsByUsername(createAppUser.name())) {
            throw ClientErrors.userNameTaken(createAppUser.name());
        }
        AppUser user = toEntity(createAppUser);
        appUserRepo.save(user);
        return toDto(user);
    }

    public AppUserDto get(String userName) {
        return appUserRepo.findByUsername(userName)
                .map(this::toDto)
                .orElseThrow(() -> ClientErrors.userNotFound(userName));
    }

    public void delete(String userName) {
        AppUser user = appUserRepo.findByUsername(userName)
                .orElseThrow(() -> ClientErrors.userNotFound(userName));
        appUserRepo.delete(user);
    }

    private AppUserDto toDto(AppUser user) {
        return new AppUserDto(user.getUsername());
    }

    private AppUser toEntity(CreateAppUserDto createAppUser) {
        String hashedPassword = WeakCrypto.hashPassword(createAppUser.password());
        return new AppUser(createAppUser.name(), hashedPassword);
    }

}
