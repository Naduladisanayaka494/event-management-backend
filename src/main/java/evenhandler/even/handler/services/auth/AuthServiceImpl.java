package evenhandler.even.handler.services.auth;

import evenhandler.even.handler.dto.SignUpRequest;
import evenhandler.even.handler.dto.UserDto;
import evenhandler.even.handler.entity.User;
import evenhandler.even.handler.enums.UserRole;
import evenhandler.even.handler.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createdCustomer(SignUpRequest signuprequest) {
        User user = new User();
        user.setName(signuprequest.getName());
        user.setEmail(signuprequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signuprequest.getPassword()));
        user.setUserRole(UserRole.ADMIN);
        User createduser  =userRepository.save(user);
        UserDto userdto = new UserDto();
        userdto.setId(createduser.getId());
        return userdto;
    }

    @Override
    public boolean hasAdminwithemail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

