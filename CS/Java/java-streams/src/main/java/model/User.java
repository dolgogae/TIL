package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class User {
    
    private int id;
    private String name;
    private String email;
    private boolean verified;
    private LocalDateTime createdAt;
    private List<Integer> friendsIds;

    public User(){}

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public List<Integer> getFriendsIds() {
        return friendsIds;
    }

    public User setFriendsIds(List<Integer> friendsIds) {
        this.friendsIds = friendsIds;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public User setVerified(boolean verified) {
        this.verified = verified;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                '}';
    }

}
