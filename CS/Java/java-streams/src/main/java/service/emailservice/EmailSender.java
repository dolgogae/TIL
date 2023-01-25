package service.emailservice;

import designpatternmodel.DUser;
import model.User;

public class EmailSender {
    private EmailProvider emailProvider;

    public EmailSender setEmailProvider(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
        return this;
    }

    public void sendEmail(DUser user){
        String email = emailProvider.getEmail(user);
        System.out.println("Sending "+ email);
    }
}
