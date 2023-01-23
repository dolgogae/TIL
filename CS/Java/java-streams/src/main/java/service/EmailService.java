package service;

import model.User;

public class EmailService {
    public void sendPlayWithFriendsEmail(User user){
        user.getEmail().ifPresent(email -> 
            System.out.println("Sending 'Play With Friends' email to" + email));
    }

    public void sendMakeWithFriendsEmail(User user){
        user.getEmail().ifPresent(email -> 
            System.out.println("Sending 'Make more friends' email to" + email));
    }

    public void sendVerifyYourEmail(User user){
        user.getEmail().ifPresent(email ->
            System.out.println("Sending 'verify your Email' email to " + email));
    }
}
