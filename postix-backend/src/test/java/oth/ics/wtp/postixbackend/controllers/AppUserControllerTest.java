package oth.ics.wtp.postixbackend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.postixbackend.dtos.AppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreatePostDto;
import oth.ics.wtp.postixbackend.dtos.SearchUserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AppUserControllerTest extends PostixControllerTestBase {
    @Autowired private AppUserController controller;
    @Autowired private PostController postController;
    @Test public void testCreateLoginLogout() {
        postController.createPost(tester(), new CreatePostDto("hello world"));
        controller.createUser(new CreateAppUserDto("user123", "123"));
        controller.logIn(mockRequest("user123", "123"));
        assertDoesNotThrow(() -> postController.listMyPosts(mockRequest("user123", "123")));
        controller.logOut(mockRequest("user123", "123"));
        assertThrows(ResponseStatusException.class, () -> postController.listMyPosts(mockRequest("user123", "123")));
    }

    @Test public void testCreateUserDuplicate() {
        controller.createUser(new CreateAppUserDto("user123", "123"));
        assertThrows(ResponseStatusException.class, () ->
                controller.createUser(new CreateAppUserDto("user123", "123")));
    }

    @Test public void testCreateGetDelete() {
        controller.createUser(new CreateAppUserDto("user123", "user123"));
        AppUserDto user = controller.getUser("user123");
        assertEquals("user123", user.name());
        controller.deleteUser("user123");
        assertThrows(ResponseStatusException.class, () -> controller.getUser("user123"));
    }

    @Test public void testDeleteUserWithPosts() {
        controller.createUser(new CreateAppUserDto("user123", "123"));
        postController.createPost((mockRequest("user123", "123")), new CreatePostDto("hello world"));
        assertDoesNotThrow(() -> controller.deleteUser("user123"));
        assertThrows(ResponseStatusException.class, () -> postController.listMyPosts(mockRequest("user123", "123")));
    }

    @Test public void testSearchUsers() {
        controller.createUser(new CreateAppUserDto("user123", "123"));
        controller.createUser(new CreateAppUserDto("user456", "456"));

        List<SearchUserDto> results = controller.searchUsers(tester(), "user");
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(u -> u.username().startsWith("user")));
    }

}
