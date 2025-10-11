package com.bikeshare.api.bike;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.bikeshare.api.bike.BikeDTOs.*;

@Tag(name = "Bikes", description = "Gestión de bicicletas")
@RestController
@RequestMapping("/api/bikes")
public class BikeController {

    private final BikeService service;

    public BikeController(BikeService service) {
        this.service = service;
    }

    @Operation(summary = "Listar bicicletas",
            description = "Filtra por estado o por propietario si se indican parámetros")
    @GetMapping
    public List<View> list(
            @Parameter(description = "Estado de la bici") @RequestParam(required = false) BikeStatus status,
            @Parameter(description = "ID del propietario") @RequestParam(required = false) Long ownerId
    ) {
        return service.list(status, ownerId).stream().map(BikeDTOs::toView).toList();
    }

    @Operation(summary = "Crear bicicleta",
            responses = @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = View.class))))
    @PostMapping
    public ResponseEntity<View> create(@Valid @RequestBody Create body) {
        Bike saved = service.create(body);
        return ResponseEntity.created(URI.create("/api/bikes/" + saved.getId()))
                .body(BikeDTOs.toView(saved));
    }

    @Operation(summary = "Obtener bicicleta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<View> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(BikeDTOs.toView(service.get(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar bicicleta (parcial)")
    @PatchMapping("/{id}")
    public ResponseEntity<View> update(@PathVariable Long id, @Valid @RequestBody Update body) {
        try {
            return ResponseEntity.ok(BikeDTOs.toView(service.update(id, body)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar bicicleta")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
