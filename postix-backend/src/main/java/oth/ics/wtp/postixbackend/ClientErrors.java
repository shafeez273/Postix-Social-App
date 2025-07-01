package oth.ics.wtp.postixbackend;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ClientErrors {
    private static final Logger logger = LoggerFactory.getLogger(ClientErrors.class);

    public static ResponseStatusException unauthorized() {
        return log(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "valid Basic credentials required"));
    }
    public static ResponseStatusException userNameTaken(String name) {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "user name already taken: " + name));
    }

    public static ResponseStatusException invalidCredentials() {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));
    }

    public static ResponseStatusException userNotFound(String name) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "user with name " + name));
    }

    public static ResponseStatusException postNotFound() {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));
    }

    public static ResponseStatusException likeNotFound() {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "like not found"));
    }
    public static ResponseStatusException cannotFollowYourself() {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot follow yourself"));
    }
    public static ResponseStatusException alreadyFollowing(String name) {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Already following " + name));
    }

    public static ResponseStatusException alreadyNotFollowing(String name) {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "Can't unfollow " + name));
    }

    public static ResponseStatusException notFollowing(String name) {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "You do not follow " + name));
    }

    public static ResponseStatusException cannotLikeOwnPost() {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "You cannot like your own post "));
    }

    public static ResponseStatusException alreadyLiked(long postId) {
        return log(new ResponseStatusException(HttpStatus.CONFLICT, "You already liked this post" + postId));
    }

    private static ResponseStatusException log(ResponseStatusException e) {
        logger.error(ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
        return e;
    }
}
