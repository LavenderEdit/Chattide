package studios.tkoh.chattide.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Studios TKOH!
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/VAADIN/**").permitAll() // Crucial for Vaadin
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Simplificado para que funcione
                .formLogin(form -> form.loginPage("/login").permitAll());

        return http.build();
    }
}
