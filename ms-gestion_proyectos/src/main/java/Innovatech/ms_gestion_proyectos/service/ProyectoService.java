package Innovatech.ms_gestion_proyectos.service;

import Innovatech.ms_gestion_proyectos.model.Proyecto;
import Innovatech.ms_gestion_proyectos.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    public List<Proyecto> getAllProyectos() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> getProyectoById(Long id) {
        return proyectoRepository.findById(id);
    }

    public Proyecto createProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public Proyecto updateProyecto(Long id, Proyecto proyectoDetails) {
        return proyectoRepository.findById(id).map(proyecto -> {
            proyecto.setNombre(proyectoDetails.getNombre());
            proyecto.setDescripcion(proyectoDetails.getDescripcion());
            proyecto.setEstado(proyectoDetails.getEstado());
            proyecto.setFechaInicio(proyectoDetails.getFechaInicio());
            proyecto.setRutResponsable(proyectoDetails.getRutResponsable());
            return proyectoRepository.save(proyecto);
        }).orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    public void deleteProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }
}
