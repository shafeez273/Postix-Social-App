package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity public class AppUser {
    @Id private String username;
    private String hashedPassword;
    @OneToMany(mappedBy = "follower") private List<Follower> followers;
    @OneToMany(mappedBy = "following") private List<Follower> following;

    public AppUser() { }

    public AppUser(String name, String hashedPassword) {
        this.username = name;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Follower> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follower> following) {
        this.following = following;
    }
}
