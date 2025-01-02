package evenhandler.even.handler.services.auth;


import evenhandler.even.handler.dto.SignUpRequest;
import evenhandler.even.handler.dto.UserDto;

public interface AuthService {
    UserDto createdCustomer(SignUpRequest signuprequest);
    boolean hasAdminwithemail(String email);
}

