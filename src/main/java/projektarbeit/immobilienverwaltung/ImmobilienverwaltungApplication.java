package projektarbeit.immobilienverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "projektarbeit.immobilienverwaltung.model")
@EnableJpaRepositories(basePackages = "projektarbeit.immobilienverwaltung.repository")
public class ImmobilienverwaltungApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImmobilienverwaltungApplication.class, args);
    }
}
