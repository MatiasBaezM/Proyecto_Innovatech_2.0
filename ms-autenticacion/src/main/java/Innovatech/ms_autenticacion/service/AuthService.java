package Innovatech.ms_autenticacion.service;

import Innovatech.ms_autenticacion.model.AuthRequest;
import Innovatech.ms_autenticacion.model.AuthResponse;
import Innovatech.ms_autenticacion.model.Usuario;
import Innovatech.ms_autenticacion.repository.UsuarioRepository;
import Innovatech.ms_autenticacion.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByRutAndClave(request.getRut(), request.getClave());
        
        if (usuarioOpt.isPresent()) {
            String token = jwtUtil.generateToken(request.getRut());
            return new AuthResponse(token);
        } else {
            throw new RuntimeException("Credenciales inválidas: RUT o Clave incorrectos");
        }
    }

    public Usuario register(Usuario usuario) {
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new RuntimeException("El usuario con este RUT ya existe");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public void deleteUser(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario updateUser(Long id, Usuario userDetails) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setNombre(userDetails.getNombre());
        user.setRut(userDetails.getRut());
        user.setRol(userDetails.getRol());
        if (userDetails.getClave() != null && !userDetails.getClave().isEmpty()) {
            user.setClave(userDetails.getClave());
        }
        
        return usuarioRepository.save(user);
    }
}
