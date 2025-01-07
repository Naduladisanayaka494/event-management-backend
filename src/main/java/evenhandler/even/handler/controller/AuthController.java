package evenhandler.even.handler.controller;


import evenhandler.even.handler.dto.AuthenticationRequest;
import evenhandler.even.handler.dto.AuthenticationResponse;
import evenhandler.even.handler.dto.SignUpRequest;
import evenhandler.even.handler.dto.UserDto;
import evenhandler.even.handler.entity.User;
import evenhandler.even.handler.repository.UserRepository;
import evenhandler.even.handler.services.auth.AuthServiceImpl;
import evenhandler.even.handler.services.jwt.UserServiceimpl;
import evenhandler.even.handler.utill.JWTUtill;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceimpl userService;
    private final JWTUtill jwtUtill;
    private  final UserRepository userRepository;

    public AuthController(AuthServiceImpl authService, AuthenticationManager authenticationManager, UserServiceimpl userService, JWTUtill jwtUtill, UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtill = jwtUtill;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest) {
        if(authService.hasAdminwithemail(signupRequest.getEmail()))
            return new ResponseEntity<>("email already exists",HttpStatus.NOT_ACCEPTABLE);
        UserDto createduserdto  =authService.createdDataEntry(signupRequest);
        if(createduserdto==null) return new ResponseEntity<>(
                "Admin not created", HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(createduserdto,HttpStatus.CREATED);

    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> signupAdmin(@RequestBody SignUpRequest signupRequest) {
        if(authService.hasAdminwithemail(signupRequest.getEmail()))
            return new ResponseEntity<>("email already exists",HttpStatus.NOT_ACCEPTABLE);
        UserDto createduserdto  =authService.createdAdmin(signupRequest);
        if(createduserdto==null) return new ResponseEntity<>(
                "Admin not created", HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(createduserdto,HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public AuthenticationResponse createauthenticationtoken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException, BadRequestException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch(BadCredentialsException e){
            throw new BadRequestException("incorrect username or passoword");
        }
        final UserDetails userDetails= userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        System.out.print(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        final String jwt = jwtUtill.generateToken(userDetails);
        AuthenticationResponse authenticationResponse= new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setUserRole(user.getUserRole());
            return userDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }
}
