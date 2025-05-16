package oth.ics.wtp.postixbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AppUser {

    @Id private String name;
    private String hashedPassword;

    public AppUser() { }

    public AppUser(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

}
