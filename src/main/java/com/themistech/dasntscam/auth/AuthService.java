package com.themistech.dasntscam.auth;

import com.themistech.dasntscam.entities.Rol;
import com.themistech.dasntscam.jwt.JwtService;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.repositories.UserRepository;
import com.themistech.dasntscam.requests.LoginRequest;
import com.themistech.dasntscam.requests.PartialRegisterRequest;
import com.themistech.dasntscam.requests.RegisterRequest;
import com.themistech.dasntscam.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Metodo de login, devuelve el token
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreoElectronico(), request.getPassword()));
        UserDetails user=userRepository.findByCorreoElectronico(request.getCorreoElectronico()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    //Metodo de registro completo, devuelve el token
    public AuthResponse register(RegisterRequest request) {
        //Creo el user
        User user = User.builder()
                .nombre(request.getNombre())
                .apellido1(request.getApellido1())
                .apellido2(request.getApellido2())
                .sexo(request.getSexo())
                .numeroDni(request.getNumeroDni())
                .correoElectronico(request.getCorreoElectronico())
                .numeroContacto(request.getNumeroContacto())
                .fechaNacimiento(request.getFechaNacimiento())
                .pais(request.getPais())
                .localidad(request.getLocalidad())
                .municipio(request.getMunicipio())
                .codigoPostal(request.getCodigoPostal())
                .rol(request.getRol())
                .contrasenaHash(passwordEncoder.encode(request.getPassword()))
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .build();

        userRepository.save(user);
        //Devolvemos el response personalizado con el token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public AuthResponse partialRegister(PartialRegisterRequest request) {
        //Creo el user
        User user = User.builder()
                .correoElectronico(request.getCorreoElectronico())
                .rol(Rol.cliente)
                .contrasenaHash(passwordEncoder.encode(request.getPassword()))
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .build();
        userRepository.save(user);

        //Devolvemos el response personalizado con el token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public AuthResponse checkUser(PartialRegisterRequest request) {
        Optional<User> user = null;
        user = userRepository.findByCorreoElectronico(request.getCorreoElectronico());
        if(user != null){
            //Devolvemos el response personalizado con el token
            return null; //Devolver que el user no existe y puede continuar
        } else {
            return null;//Devolver error
        }
    }
}
