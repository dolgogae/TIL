package model;

public class User {
    
    private int id;
    private String name;
    private String email;
    private boolean verified;

    public User(){}

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getEmail() {
        return this.email;
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

}
