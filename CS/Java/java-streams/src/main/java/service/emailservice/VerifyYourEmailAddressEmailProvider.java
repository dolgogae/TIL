package service.emailservice;

import designpatternmodel.DUser;
import model.User;

public class VerifyYourEmailAddressEmailProvider implements EmailProvider{

    @Override
    public String getEmail(DUser user) {
        // TODO Auto-generated method stub
        return "'Verify Your Email Address' email for " + user.getName();
    }
    
}
