package service.userservice;

import designpatternmodel.DUser;

public class InternalUserService extends AbstractUserService{

    @Override
    protected boolean validateUser(DUser user) {
        // TODO Auto-generated method stub
        System.out.println("validating internal user " + user.getName());
        return true;
    }

    @Override
    protected void writeToDb(DUser user) {
        // TODO Auto-generated method stub
        System.out.println("Writing user "+ user.getName() + " to internal DB");
    }
    
}
