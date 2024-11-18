package ercankara.uygulamam_backhad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sadece belirli kaynaklara izin ver
        configuration.setAllowedOrigins(List.of("http://10.0.2.2:8080", "http://localhost:8080"));

        // Belirli HTTP metotlarına izin ver
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Gerekli başlıklara izin ver
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        // Kimlik bilgileri için izin ver
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
