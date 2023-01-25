package service.emailservice;

import designpatternmodel.DUser;
import model.User;

public class MakeMoreFriendEmailProvider implements EmailProvider{

    @Override
    public String getEmail(DUser user) {
        // TODO Auto-generated method stub
        return "'Make More Friends' email for " + user.getEmail();
    }
    
}
