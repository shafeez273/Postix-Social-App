package oth.ics.wtp.postixbackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import oth.ics.wtp.postixbackend.WeakCrypto;
import oth.ics.wtp.postixbackend.entities.AppUser;
import oth.ics.wtp.postixbackend.repositories.AppUserRepository;

import java.nio.charset.StandardCharsets;
import java.util.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class PostixControllerTestBase {
    protected static final String USER_USERNAME = "tester";
    protected static final String USER_PASSWORD = "pw";


    @Autowired protected AppUserRepository appUserRepo;

    private final Map<String, HttpSession> sessions = new HashMap<>();

    @BeforeEach public void beforeEach() {
        createAppUser(USER_USERNAME, USER_PASSWORD);
        sessions.clear();
    }

    private void createAppUser(String userName, String password) {
        String passwordHash = WeakCrypto.hashPassword(password);
        AppUser user = new AppUser(userName, passwordHash);
        appUserRepo.save(user);
    }

    protected MockHttpServletRequest mockRequest(String username, String password) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // restore session of same tester, if any
        if (!sessions.containsKey(username)) {
            request.addHeader(HttpHeaders.AUTHORIZATION, basic(username, password));
            sessions.put(username, request.getSession());
        } else {
            request.setSession(sessions.get(username));
        }
        return request;
    }

    protected HttpServletRequest tester() {
        return mockRequest(USER_USERNAME, USER_PASSWORD);
    }

    protected String basic(String userName, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

}
