package service.userservice;

import java.util.function.Consumer;
import java.util.function.Predicate;

import designpatternmodel.DUser;

public class UserServiceFunctionalWay {
    private final Predicate<DUser> validateUser;
    private final Consumer<DUser> writeToDb;

    public UserServiceFunctionalWay(Predicate<DUser> validateUser, Consumer<DUser> writeToDb){
        this.validateUser = validateUser;
        this.writeToDb = writeToDb;
    }

    public void createUser(DUser user){
        if(validateUser.test(user)){
            writeToDb.accept(user);
        } else{
            System.out.println("Cannot create user");
        }
    }
}
