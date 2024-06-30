package projektarbeit.immobilienverwaltung.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.UserRepository;
import projektarbeit.immobilienverwaltung.ui.views.login.LoginView;

/**
 * Sicherheitskonfiguration für die Anwendung.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final UserRepository userRepository;

    /**
     * Konstruktor für die SecurityConfig-Klasse.
     *
     * @param userRepository Das Repository für Benutzer.
     */
    @Autowired
    public SecurityConfig(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Konfiguriert die HTTP-Sicherheitsanforderungen für die Anwendung.
     *
     * @param http das HttpSecurity-Objekt zum Konfigurieren der Sicherheitsanforderungen.
     * @throws Exception wenn ein Fehler bei der Konfiguration auftritt.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setLoginView(http, LoginView.class);
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/");

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/styles/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/**")).permitAll());

        super.configure(http);
    }

    /**
     * Stellt den UserDetailsService als Bean bereit.
     *
     * @return der UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toArray(String[]::new))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        };
    }

    /**
     * Stellt den PasswordEncoder als Bean bereit.
     *
     * @return der PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}