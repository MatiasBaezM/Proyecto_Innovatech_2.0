package Innovatech.ms_recursos_colaboraciones.service;

import Innovatech.ms_recursos_colaboraciones.model.Habilidad;
import Innovatech.ms_recursos_colaboraciones.repository.HabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabilidadService {

    private final HabilidadRepository habilidadRepository;

    public List<Habilidad> findAll() {
        return habilidadRepository.findAll();
    }

    public Optional<Habilidad> findById(Long id) {
        return habilidadRepository.findById(id);
    }

    public Habilidad save(Habilidad habilidad) {
        return habilidadRepository.save(habilidad);
    }

    public void deleteById(Long id) {
        habilidadRepository.deleteById(id);
    }
}
