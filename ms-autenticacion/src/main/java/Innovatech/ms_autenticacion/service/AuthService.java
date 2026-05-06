package Innovatech.ms_autenticacion.service;

import Innovatech.ms_autenticacion.model.AuthRequest;
import Innovatech.ms_autenticacion.model.AuthResponse;
import Innovatech.ms_autenticacion.model.Usuario;
import Innovatech.ms_autenticacion.repository.UsuarioRepository;
import Innovatech.ms_autenticacion.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByRutAndNombre(request.getRut(), request.getNombre());
        
        if (usuarioOpt.isPresent()) {
            String token = jwtUtil.generateToken(request.getRut());
            return new AuthResponse(token);
        } else {
            throw new RuntimeException("Credenciales inválidas: RUT o Nombre incorrectos");
        }
    }

    public Usuario register(Usuario usuario) {
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new RuntimeException("El usuario con este RUT ya existe");
        }
        return usuarioRepository.save(usuario);
    }
}
