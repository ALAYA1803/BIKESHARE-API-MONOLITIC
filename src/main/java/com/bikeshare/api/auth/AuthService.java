package com.bikeshare.api.auth;

import com.bikeshare.api.profile.ProfileService;
import com.bikeshare.api.user.User;
import com.bikeshare.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileService profileService;
    // En un proyecto real, inyectarías un PasswordEncoder aquí.
    // private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // En un proyecto real, deberías validar si el email ya existe.
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("El correo electrónico ya está en uso.");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                // NOTA: ¡Encripta esto en un proyecto real!
                // .passwordHash(passwordEncoder.encode(request.password()))
                .passwordHash(request.password())
                .isOwner(request.isOwner())
                .build();
        User savedUser = userRepository.save(user);

        // Llama al ProfileService para crear el perfil correspondiente
        profileService.createProfileForUser(savedUser);

        // Aquí generarías un token JWT real basado en el `savedUser`
        return new AuthResponse("token_jwt_de_ejemplo_para_" + savedUser.getEmail());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña inválidos"));

        // NOTA: ¡Compara hashes en un proyecto real!
        // if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
        if (!request.password().equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("Usuario o contraseña inválidos");
        }
        // Aquí generarías un token JWT real
        return new AuthResponse("token_jwt_de_ejemplo_para_" + user.getEmail());
    }
}