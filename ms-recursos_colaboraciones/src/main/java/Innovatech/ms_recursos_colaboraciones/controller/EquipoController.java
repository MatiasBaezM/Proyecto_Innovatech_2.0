package Innovatech.ms_recursos_colaboraciones.controller;

import Innovatech.ms_recursos_colaboraciones.model.Equipo;
import Innovatech.ms_recursos_colaboraciones.service.EquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<Equipo>> getAllEquipos() {
        return ResponseEntity.ok(equipoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> getEquipoById(@PathVariable Long id) {
        return equipoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Equipo> createEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.save(equipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> updateEquipo(@PathVariable Long id, @RequestBody Equipo equipo) {
        return equipoService.findById(id)
                .map(existing -> {
                    equipo.setId(existing.getId());
                    return ResponseEntity.ok(equipoService.save(equipo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
        if (equipoService.findById(id).isPresent()) {
            equipoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
