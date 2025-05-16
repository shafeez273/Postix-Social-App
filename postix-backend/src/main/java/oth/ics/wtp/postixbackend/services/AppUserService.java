package oth.ics.wtp.postixbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.WeakCrypto;
import oth.ics.wtp.postixbackend.dtos.AppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service public class AppUserService {
    private final AppUserRepository appUserRepo;

    @Autowired public AppUserService(AppUserRepository appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public List<AppUserDto> list() {
        return StreamSupport.stream(appUserRepo.findAll().spliterator(), false).map(this::toDto).toList();
    }

    public AppUserDto create(CreateAppUserDto createAppUser) {
        if (createAppUser.name() == null || createAppUser.name().isEmpty() ||
            createAppUser.password() == null || createAppUser.password().isEmpty()) {
            throw ClientErrors.invalidCredentials();
        }
        if (appUserRepo.existsByName(createAppUser.name())) {
            throw ClientErrors.userNameTaken(createAppUser.name());
        }
        AppUser user = toEntity(createAppUser);
        appUserRepo.save(user);
        return toDto(user);
    }

    public AppUserDto get(String userName) {
        return appUserRepo.findByName(userName)
                .map(this::toDto)
                .orElseThrow(() -> ClientErrors.userNotFound(userName));
    }

    public void delete(String userName) {
        AppUser user = appUserRepo.findByName(userName)
                .orElseThrow(() -> ClientErrors.userNotFound(userName));
        appUserRepo.delete(user);
    }

    private AppUserDto toDto(AppUser user) {
        return new AppUserDto(user.getName());
    }

    private AppUser toEntity(CreateAppUserDto createAppUser) {
        String hashedPassword = WeakCrypto.hashPassword(createAppUser.password());
        return new AppUser(createAppUser.name(), hashedPassword);
    }

}
