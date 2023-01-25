package service.emailservice;

import designpatternmodel.DUser;
import model.User;

public interface EmailProvider {
    String getEmail(DUser user);
}
