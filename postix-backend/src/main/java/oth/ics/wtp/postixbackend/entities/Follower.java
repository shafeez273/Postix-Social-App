package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.*;

@Entity public class Follower {
    @Id @GeneratedValue private long id;
    @ManyToOne private AppUser follower;
    @ManyToOne private AppUser following;

    public Follower() { }

    public Follower(AppUser follower, AppUser following) {
        this.follower = follower;
        this.following = following;
    }

    public long getId() {
        return id;
    }
    public AppUser getFollower() {
        return follower;
    }

    public AppUser getFollowing() {
        return following;
    }
}
