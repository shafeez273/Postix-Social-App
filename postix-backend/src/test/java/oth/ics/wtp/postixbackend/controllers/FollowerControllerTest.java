package oth.ics.wtp.postixbackend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.dtos.FollowerDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FollowerControllerTest extends PostixControllerTestBase {
    @Autowired private AppUserController userController;
    @Autowired private FollowerController controller;
    @BeforeEach @Override public void beforeEach() {
        super.beforeEach();
        userController.createUser(new CreateAppUserDto("user123", "123"));
        userController.createUser(new CreateAppUserDto("user456", "456"));
        userController.createUser(new CreateAppUserDto("user789", "789"));
    }

    @Test public void testFollow() {
        FollowerDto dto = controller.createFollower((mockRequest("user123", "123")), "user456");
        assertEquals("user456", dto.followingName());

        assertThrows(ResponseStatusException.class, () -> controller.createFollower((mockRequest("user123", "123")), "user123"));

        assertThrows(ResponseStatusException.class, () -> controller.createFollower((mockRequest("user123", "123")), "user456"));
    }

    @Test public void testUnfollow() {
        FollowerDto dto = controller.createFollower((mockRequest("user123", "123")), "user456");
        assertEquals("user456", dto.followingName());

        assertDoesNotThrow(() -> controller.removeFollower((mockRequest("user123", "123")), "user456"));

        assertThrows(ResponseStatusException.class, () -> controller.removeFollower((mockRequest("user123", "123")), "user789"));
    }

    @Test public void testGetFollowers() {
        controller.createFollower((mockRequest("user123", "123")), "user456");
        controller.createFollower((mockRequest("user789", "789")), "user456");
        List<FollowerDto> followers = controller.getFollowers("user456");
        assertEquals(2, followers.size());
    }

    @Test public void testGetFollowing() {
        controller.createFollower((mockRequest("user456", "456")), "user123");
        controller.createFollower((mockRequest("user456", "456")), "user789");
        List<FollowerDto> following = controller.getFollowing("user456");
        assertEquals(2, following.size());
    }

    @Test public void testIsFollowing() {
        controller.createFollower(mockRequest("user123", "123"), "user456");
        assertTrue(controller.isFollowing("user123", "user456"));

        assertFalse(controller.isFollowing("user123", "user789"));

    }

}
