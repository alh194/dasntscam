package com.themistech.dasntscam.auth;

import com.themistech.dasntscam.entities.Cliente;
import com.themistech.dasntscam.entities.Perito;
import com.themistech.dasntscam.enums.Rol;
import com.themistech.dasntscam.jwt.JwtService;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.repositories.ClienteRepository;
import com.themistech.dasntscam.repositories.PeritoRepository;
import com.themistech.dasntscam.repositories.UserRepository;
import com.themistech.dasntscam.requests.LoginRequest;
import com.themistech.dasntscam.requests.clienteRegister;
import com.themistech.dasntscam.requests.pertioRegister;
import com.themistech.dasntscam.responses.AuthResponse;
import com.themistech.dasntscam.responses.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final PeritoRepository peritoRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Metodo de login, devuelve el token
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreoElectronico(), request.getPassword()));
        User user=userRepository.findByCorreoElectronico(request.getCorreoElectronico()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    //Metodo de registro completo, devuelve el token
    public AuthResponse pertioRegister(pertioRegister request) {
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
                .rol(Rol.perito)
                .contrasenaHash(passwordEncoder.encode(request.getPassword()))
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .build();

        userRepository.save(user);

        //Creamos en la tabla perito el registro correspondiente
        Perito perito = new Perito();
        perito.setUsuario(user);
        perito.setCodigoRegistro("CodigoRegistroDummy");//TODO OJO ESTO ES TEMPORAL, esta por ver cuando se cumplimenta esta informacion, si en el registro o despues
        perito.setColegioProfesional("ColegioProfesionalDummy");
        perito.setNumeroColegiado("NumeroColegiadoDummy");
        peritoRepository.save(perito);

        //Devolvemos el response personalizado con el token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public AuthResponse clienteRegister(clienteRegister request) {
        //Creo el user
        User user = User.builder()
                .correoElectronico(request.getCorreoElectronico())
                .rol(Rol.cliente)
                .contrasenaHash(passwordEncoder.encode(request.getPassword()))
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .build();
        userRepository.save(user);

        //Creamos en la tabla cliente el registro correspondiente
        Cliente cliente = new Cliente();
        cliente.setUsuario(user);
        cliente.setLibroVerde("LibroVerdeDummy".getBytes());//TODO OJO ESTO ES TEMPORAL, esta por ver cuando se cumplimenta esta informacion, si en el registro o despues
        cliente.setPolizaSeguro("PolizaSeguroDummy".getBytes());
        clienteRepository.save(cliente);

        //Devolvemos el response personalizado con el token
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    //Recuperar el rol con el token
    public RoleResponse getUserRoleWithToken(String token) {
        User user = getUserWithToken(token);
        return RoleResponse.builder()
                .StringRole(String.valueOf(user.getRol()))
                .build();
    }

    //Recuperar el usuario a partir del rol
    public User getUserWithToken(String token) {
        String userName = jwtService.getUsernameFromToken(token);
        return userRepository.findByCorreoElectronico(userName).orElseThrow();
    }
}
