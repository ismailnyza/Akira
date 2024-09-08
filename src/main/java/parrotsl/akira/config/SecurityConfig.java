package parrotsl.akira.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        .csrf().disable()  // Disable CSRF since you're using JWT and stateless sessions
        .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/auth/**")
                .permitAll()  // Permit access to login and authentication
                .requestMatchers("/v3/**").permitAll()  // Permit access to login and authentication
                .requestMatchers("swagger-ui.html")
                .permitAll()  // Permit access to login and authentication
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Secure admin endpoints
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // Secure user endpoints
                .anyRequest().authenticated()
        )
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}