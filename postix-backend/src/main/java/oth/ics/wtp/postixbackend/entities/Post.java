package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity public class Post {
    @Id @GeneratedValue private long id;
    private String message;
    private Instant timestamp;

    public Post() { }

    public Post(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
