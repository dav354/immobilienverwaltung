package projektarbeit.immobilienverwaltung;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImmobilienverwaltungApplication {
    private static final Logger log = LoggerFactory.getLogger(ImmobilienverwaltungApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ImmobilienverwaltungApplication.class, args);
    }
}