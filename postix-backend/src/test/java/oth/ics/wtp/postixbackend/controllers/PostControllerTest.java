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
import oth.ics.wtp.postixbackend.dtos.PostDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostControllerTest extends PostixControllerTestBase{
    @Autowired private PostController controller;
    @Autowired private AppUserController userController;
    @Autowired private FollowerController followerController;

    @BeforeEach @Override public void beforeEach() {
        super.beforeEach();
        userController.createUser(new CreateAppUserDto("user123", "123"));
        userController.createUser(new CreateAppUserDto("user456", "456"));
    }

    @Test public void testListEmpty() {
        assertTrue(controller.listMyPosts(mockRequest("user123", "123")).isEmpty());
        assertTrue(controller.listAllPosts(mockRequest("user123", "123")).isEmpty());
    }

    @Test public void testCreateDelete() {
        long id1 = controller.createPost(mockRequest("user123", "123"), new CreatePostDto("hello")).id();
        long id2 = controller.createPost(mockRequest("user123", "123"), new CreatePostDto("world")).id();
        controller.deletePost(mockRequest("user123", "123"), id2);
        List<PostDto> postDtos = controller.listMyPosts(mockRequest("user123", "123"));
        assertEquals(id1, postDtos.getFirst().id());
        assertEquals(1, postDtos.size());

        long id3 = controller.createPost(mockRequest("user456", "456"), new CreatePostDto("hello world")).id();
        assertThrows(ResponseStatusException.class, () ->
                controller.deletePost(mockRequest("user123", "123"), id3));
    }
    @Test public void testListOwnPosts() {
        long id1 = controller.createPost(mockRequest("user123", "123"), new CreatePostDto("This is my first post.")).id();
        long id2 = controller.createPost(mockRequest("user123", "123"), new CreatePostDto("This is my second post.")).id();
        List<PostDto> postDtos = controller.listMyPosts(mockRequest("user123", "123"));
        assertEquals(id2, postDtos.getFirst().id());
        assertEquals(id1, postDtos.getLast().id());
        assertEquals(2, postDtos.size());
    }

    @Test public void testListUserPosts() {
        long id1 = controller.createPost(mockRequest("user123", "123"), new CreatePostDto("This is my first post.")).id();
        followerController.createFollower(mockRequest("user456", "456"), "user123");
        List<PostDto> dtos = controller.listUserPosts("user123", mockRequest("user456", "456"));
        assertEquals(id1, dtos.getFirst().id());
        assertEquals(1, dtos.size());

        userController.createUser(new CreateAppUserDto("user789", "789"));
        assertThrows(ResponseStatusException.class, () -> controller.listUserPosts("user123", mockRequest("user789", "789")));
    }


    @Test public void testListTimeline() {
        controller.createPost(mockRequest("user123", "123"), new CreatePostDto("I'm user123"));
        controller.createPost(mockRequest("user456", "456"), new CreatePostDto("I'm user456"));

        followerController.createFollower(mockRequest("user123", "123"), "user456");

        List<PostDto> timeline = controller.listAllPosts(mockRequest("user123", "123"));
        assertEquals(1, timeline.size());
        assertEquals("I'm user456", timeline.getFirst().message());

        followerController.removeFollower(mockRequest("user123", "123"), "user456");
        List<PostDto> timelineEmpty = controller.listAllPosts(mockRequest("user123", "123"));
        assertTrue(timelineEmpty.isEmpty());
    }
}
