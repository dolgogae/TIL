package designpatternmodel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DUser {
    
    private int id;
    private String name;
    private String email;
    private boolean verified;
    private LocalDateTime createdAt;
    private List<Integer> friendsIds = new ArrayList<>();

    public DUser(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.verified = builder.verified;
        this.createdAt = builder.createdAt;
        this.friendsIds = builder.friendsIds;
    }

    public static Builder builder(int id, String name){
        return new Builder(id, name);
    }

    public static class Builder{
        private int id;
        private String name;
        public String email;
        public boolean verified;
        public LocalDateTime createdAt;
        public List<Integer> friendsIds;

        private Builder(int id, String name){
            this.id = id;
            this.name = name;
        }

        public Builder with(Consumer<Builder> consumer){
            consumer.accept(this);
            return this;
        }

        public DUser build(){
            return new DUser(this);
        }
    }

    public List<Integer> getFriendsIds() {
        return friendsIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public boolean isVerified() {
        return this.verified;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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
