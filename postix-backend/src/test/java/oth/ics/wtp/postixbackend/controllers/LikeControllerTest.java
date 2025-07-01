package oth.ics.wtp.postixbackend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.postixbackend.dtos.CreateAppUserDto;
import oth.ics.wtp.postixbackend.dtos.CreatePostDto;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LikeControllerTest extends PostixControllerTestBase{
    @Autowired private LikeController controller;
    @Autowired private PostController postController;
    @Autowired private AppUserController userController;

    private long postId;
    @BeforeEach @Override public void beforeEach() {
        super.beforeEach();
        postId = postController.createPost(tester(), new CreatePostDto("hello world")).id();
    }

    @Test public void testInit() {
        userController.createUser(new CreateAppUserDto("user456", "456"));
        assertFalse(controller.isLiked(mockRequest("user456", "456"),postId));
    }
    @Test public void testLike() {
        userController.createUser(new CreateAppUserDto("user456", "456"));

        // like another tester's post successfully
        controller.likePost((mockRequest("user456", "456")), postId);
        assertTrue(controller.isLiked((mockRequest("user456", "456")), postId));

        // can't like your own post
        assertThrows(ResponseStatusException.class, () -> controller.likePost(tester(), postId));

        // can't like twice
        assertThrows(ResponseStatusException.class, () -> controller.likePost((mockRequest("user456", "456")), postId));     }

    @Test public void testUnlikePost() {
        userController.createUser(new CreateAppUserDto("user456", "456"));

        // like another tester's post successfully
        controller.likePost((mockRequest("user456", "456")), postId);
        assertTrue(controller.isLiked((mockRequest("user456", "456")), postId));

        // unlike another tester's post successfully
        controller.unlikePost((mockRequest("user456", "456")), postId);
        assertFalse(controller.isLiked((mockRequest("user456", "456")), postId));

        // can't unlike post you never liked
        userController.createUser(new CreateAppUserDto("user789", "789"));
        assertThrows(ResponseStatusException.class, () -> controller.unlikePost((mockRequest("user789", "789")), postId));
    }
}
