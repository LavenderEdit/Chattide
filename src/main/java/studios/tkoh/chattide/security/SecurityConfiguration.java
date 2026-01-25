package studios.tkoh.chattide.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Studios TKOH!
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. Deshabilitar CSRF para permitir peticiones UIDL de Vaadin
        http.csrf(csrf -> csrf.disable());

        // 2. Configuración de Headers para evitar bloqueos de CSS y Frames
        http.headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
                // Deshabilitar CSP restrictivo en desarrollo para permitir estilos inyectados por Vaadin
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:; connect-src 'self' ws: wss:;"))
        );

        http.authorizeHttpRequests(auth -> auth
                // Permitir explícitamente rutas de navegación y API
                .requestMatchers("/login", "/register", "/api/**", "/h2-console/**").permitAll()
                // Permitir la raíz y CUALQUIER petición que contenga parámetros de Vaadin (v-r=init, v-r=uidl, etc.)
                // Esto es vital para evitar el ERR_TOO_MANY_REDIRECTS
                .requestMatchers("/").permitAll()
                .requestMatchers("/**?v-r=**").permitAll()
                // Permitir archivos estáticos por extensión en cualquier nivel de carpeta
                .requestMatchers("/**/*.js", "/**/*.css", "/**/*.json", "/**/*.png", "/**/*.jpg", "/**/*.svg").permitAll()
                // Requerir autenticación para el resto
                .anyRequest().authenticated()
        );

        // Configuración de Login
        http.formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
        );

        http.logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/VAADIN/**",
                "/themes/**",
                "/webjars/**",
                "/sw.js",
                "/manifest.webmanifest",
                "/icons/**",
                "/images/**",
                "/line-awesome/**",
                "/styles/**",
                "/sw-runtime-resources-precache.js",
                "/favicon.ico",
                "/offline.html"
        );
    }
}
