package blogging.service;

import blogging.dto.JwtAuthResponse;
import blogging.dto.LoginDto;
import blogging.dto.RegisterDto;

public interface AuthService {
    JwtAuthResponse login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
