package ercankara.uygulamam_backhad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;

    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // Yeni sınıfı kullan
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("users/register", "users/login").permitAll()
                        .requestMatchers("/lands").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/lands/**").permitAll()
                        .requestMatchers("/categories/**").permitAll()  // Kategoriler herkese açık
                        .requestMatchers("/api/sowings/**").permitAll()
                        .requestMatchers("/plants/by-category","/plants/detail/").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic();

        return http.build();
    }
}
