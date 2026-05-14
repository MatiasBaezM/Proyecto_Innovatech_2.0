package Innovatech.ms_recursos_colaboraciones.config;

import Innovatech.ms_recursos_colaboraciones.model.Trabajador;
import Innovatech.ms_recursos_colaboraciones.repository.TrabajadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(TrabajadorRepository trabajadorRepository) {
        return args -> {
            if (trabajadorRepository.count() == 0) {
                Trabajador t1 = Trabajador.builder()
                        .rut("11.111.111-1")
                        .nombre("Juan Pérez")
                        .email("juan@innovatech.cl")
                        .cargo("Desarrollador Backend")
                        .departamento("Ingeniería")
                        .tarifaHora(25.0)
                        .build();

                Trabajador t2 = Trabajador.builder()
                        .rut("22.222.222-2")
                        .nombre("María Silva")
                        .email("maria@innovatech.cl")
                        .cargo("Diseñadora UX/UI")
                        .departamento("Diseño")
                        .tarifaHora(22.0)
                        .build();

                trabajadorRepository.saveAll(Arrays.asList(t1, t2));
            }
        };
    }
}
