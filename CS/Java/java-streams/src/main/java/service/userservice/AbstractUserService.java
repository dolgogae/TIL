package service.userservice;

import designpatternmodel.DUser;

public abstract class AbstractUserService {
    protected abstract boolean validateUser(DUser user);

    protected abstract void writeToDb(DUser user);

    public void createUser(DUser user){
        if(validateUser(user)){
            writeToDb(user);
        } else{
            System.out.println("Cannot create user");
        }
    }
}
