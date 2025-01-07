package evenhandler.even.handler.services.auth;


import evenhandler.even.handler.dto.SignUpRequest;
import evenhandler.even.handler.dto.UserDto;

public interface AuthService {
    UserDto createdDataEntry(SignUpRequest signuprequest);
    UserDto createdAdmin(SignUpRequest signuprequest);
    boolean hasAdminwithemail(String email);
}

