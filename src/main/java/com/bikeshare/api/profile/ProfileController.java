package com.bikeshare.api.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "Gestión de perfiles de usuario")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Obtener perfil por ID de usuario", description = "Devuelve el perfil completo (propietario o arrendatario) de un usuario.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(profileService.getProfileByUserId(userId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // NOTA: El endpoint para /me (usuario autenticado) se añadiría aquí
    // una vez que implementes la seguridad con tokens. Por ahora, usamos el ID.
}