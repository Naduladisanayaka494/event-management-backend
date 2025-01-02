package evenhandler.even.handler.dto;


import evenhandler.even.handler.enums.UserRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserDto {



    private Long id;
    private String name;
    private String email;

    private UserRole userRole;

}

