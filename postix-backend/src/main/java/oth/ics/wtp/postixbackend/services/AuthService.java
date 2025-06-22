package oth.ics.wtp.postixbackend.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import oth.ics.wtp.postixbackend.ClientErrors;
import oth.ics.wtp.postixbackend.WeakCrypto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;

import java.util.Optional;

@Service public class AuthService {
    private static final String SESSION_USER_NAME = "postix-session-user-name";

    private final AppUserRepository appUserRepo;

    @Autowired public AuthService(AppUserRepository appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public AppUser getAuthenticatedUser(HttpServletRequest request) {
        Object sessionUserName = request.getSession().getAttribute(SESSION_USER_NAME);
        if (sessionUserName instanceof String userName) {
            Optional<AppUser> user = appUserRepo.findByUsername(userName);
            if (user.isPresent()) {
                return user.get();
            } else {
                logOut(request);
                throw ClientErrors.unauthorized();
            }
        }
        return logIn(request);
    }

    public AppUser logIn(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String decoded = WeakCrypto.base64decode(authHeader.substring(authHeader.indexOf(" ") + 1));
            String[] parts = decoded.split(":");
            String userName = parts[0];
            String password = parts[1];
            String hashedPassword = WeakCrypto.hashPassword(password);
            AppUser user = appUserRepo.findByUsername(userName).orElseThrow();
            if (!user.getHashedPassword().equals(hashedPassword)) {
                throw new Exception();
            }
            request.getSession().setAttribute(SESSION_USER_NAME, userName);
            return user;
        } catch (Exception e) {
            logOut(request);
            throw ClientErrors.unauthorized();
        }
    }

    public void logOut(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_USER_NAME, null);
    }


}
