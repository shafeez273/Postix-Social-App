package oth.ics.wtp.postixbackend;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ClientErrors {
    private static final Logger logger = LoggerFactory.getLogger(ClientErrors.class);

    public static ResponseStatusException userNameTaken(String name) {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "user name already taken: " + name));
    }

    public static ResponseStatusException invalidCredentials() {
        return log(new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));
    }

    public static ResponseStatusException userNotFound(String name) {
        return log(new ResponseStatusException(HttpStatus.NOT_FOUND, "user with name " + name));
    }

    private static ResponseStatusException log(ResponseStatusException e) {
        logger.error(ExceptionUtils.getMessage(e) + "\n" + ExceptionUtils.getStackTrace(e));
        return e;
    }

}
