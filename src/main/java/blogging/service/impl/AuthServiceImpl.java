package blogging.service.impl;

import blogging.dto.JwtAuthResponse;
import blogging.dto.LoginDto;
import blogging.dto.RegisterDto;
import blogging.entity.Role;
import blogging.entity.User;
import blogging.exception.BlogAPIException;
import blogging.exception.ResourceNotFoundException;
import blogging.repository.RoleRepository;
import blogging.repository.UserRepository;
import blogging.security.JwtTokenProvider;
import blogging.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(token);
        return response;
    }

    @Override
    public String register(RegisterDto registerDto) {
        //Check if username already present in db
        if(userRepository.existsByUsername(registerDto.getUsername()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!");
        //Check if user email already present in db
        if(userRepository.existsByEmail(registerDto.getEmail()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!");

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        //Setting the role to the user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User Registered Successfully";
    }
}
