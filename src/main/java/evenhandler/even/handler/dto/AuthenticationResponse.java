package evenhandler.even.handler.dto;



import evenhandler.even.handler.enums.UserRole;
import lombok.Data;


@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private Long userId;
}
