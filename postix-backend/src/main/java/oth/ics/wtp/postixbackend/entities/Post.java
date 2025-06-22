package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.List;

@Entity public class Post {
    @Id @GeneratedValue private long id;
    private String message;
    private Instant timestamp;
    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE) private AppUser user;
    @OneToMany(mappedBy = "post") private List<Like> likes;

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

    public AppUser getUser() {
        return user;
    }
}
