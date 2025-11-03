package com.bikeshare.bikeavailability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BikeAvailabilityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BikeAvailabilityServiceApplication.class, args);
    }
}

@RestController
class AvailabilityController {
    @GetMapping("/availability")
    public Map<String, Object> getAvailability(@RequestParam(defaultValue = "urbana") String tipo,
                                               @RequestParam(defaultValue = "centro") String ubicacion) {
        Map<String, Object> response = new HashMap<>();
        // Simulación de disponibilidad
        int cantidad = ("urbana".equals(tipo) && "centro".equals(ubicacion)) ? 5 : 2;
        response.put("tipo", tipo);
        response.put("ubicacion", ubicacion);
        response.put("disponibles", cantidad);
        return response;
    }
}

