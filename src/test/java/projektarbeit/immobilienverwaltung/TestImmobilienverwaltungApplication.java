package projektarbeit.immobilienverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestImmobilienverwaltungApplication {

	public static void main(String[] args) {
		SpringApplication.from(ImmobilienverwaltungApplication::main).with(TestImmobilienverwaltungApplication.class).run(args);
	}


}