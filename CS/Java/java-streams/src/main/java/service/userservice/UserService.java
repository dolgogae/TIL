package service.userservice;

import designpatternmodel.DUser;

public class UserService extends AbstractUserService {

    @Override
    protected boolean validateUser(DUser user) {
        // TODO Auto-generated method stub
        System.out.println("Validating user "+ user.getName());
        return user.getName() != null && user.getEmail().isPresent();
    }

    @Override
    protected void writeToDb(DUser user) {
        // TODO Auto-generated method stub
        System.out.println("Writing user "+user.getName()+" to DB");
    }
    
}
