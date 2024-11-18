package ercankara.uygulamam_backhad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Güvenlik yapılandırması
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF'yi devre dışı bırak
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS yapılandırmasını ekle
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("users/register", "users/login").permitAll()  // Register ve login herkese açık
                        .requestMatchers("/users/").permitAll()  // Register ve login herkese açık
                        .requestMatchers("/lands/**").authenticated()  // Lands ile ilgili tüm istekler kimlik doğrulama gerektirir
                        .requestMatchers("/sowings/**").authenticated()  // Sowing ile ilgili tüm istekler kimlik doğrulama gerektirir
                        .requestMatchers("/harvests/**").authenticated()  // Harvest ile ilgili tüm istekler kimlik doğrulama gerektirir
                        .anyRequest().authenticated()  // Diğer tüm istekler kimlik doğrulaması gerektirir
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless oturum yönetimi
                .httpBasic();  // HTTP Basic Authentication

        return http.build();
    }

    // CORS yapılandırması (opsiyonel, ihtiyacınıza göre özelleştirebilirsiniz)
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Her kaynağa izin ver
        configuration.addAllowedMethod("*"); // Her HTTP metoduna izin ver
        configuration.addAllowedHeader("*"); // Her başlığa izin ver
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
