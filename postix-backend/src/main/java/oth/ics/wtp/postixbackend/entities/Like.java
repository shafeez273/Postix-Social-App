package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "post_like")
public class Like {
    @Id @GeneratedValue private long id;
    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE) private AppUser user;
    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE) private Post post;

    public Like() { }

    public Like(AppUser user, Post post) {
        this.user = user;
        this.post = post;
    }

    public long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
