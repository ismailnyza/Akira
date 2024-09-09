package parrotsl.akira.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import parrotsl.akira.service.CustomUserDetailsService;
import parrotsl.akira.utils.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtRequestFilter jwtRequestFilter;
  private final CustomUserDetailsService customUserDetailsService;

  @Autowired
  public SecurityConfig(JwtRequestFilter jwtRequestFilter,
      CustomUserDetailsService customUserDetailsService) {
    this.jwtRequestFilter = jwtRequestFilter;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/api/auth/**").permitAll() // Public path
            .requestMatchers("/v3/**").permitAll()       // Public path
            .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI should be public
            .requestMatchers("/admin/**").hasAuthority("ADMIN")  // Protected admin path
            .requestMatchers("/api/User/Create").permitAll()  // Protected admin path
            .requestMatchers("/api/User/**").hasAuthority("ROLE_ADMIN")  // Protected admin path
            .anyRequest().authenticated() // All other requests require authentication
        )
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}