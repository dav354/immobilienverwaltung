package projektarbeit.immobilienverwaltung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/*
Disables the default spring boot security login form on all Endpoints
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Beginn der Konfiguration der Autorisierung
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Alle Anfragen erlauben
                )
                .csrf(AbstractHttpConfigurer::disable); // CSRF-Schutz deaktivieren

        return http.build();
    }
}