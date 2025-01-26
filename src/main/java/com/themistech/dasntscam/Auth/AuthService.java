package com.themistech.dasntscam.Auth;

import com.themistech.dasntscam.Jwt.JwtService;
import com.themistech.dasntscam.User.Role;
import com.themistech.dasntscam.User.User;
import com.themistech.dasntscam.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        return null;
    }

    public AuthResponse register(RegisterRequest request) {
        //Recuperamos los datos del request de registro
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.lastname)
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        //Devolvemos el response personalizado con el token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
