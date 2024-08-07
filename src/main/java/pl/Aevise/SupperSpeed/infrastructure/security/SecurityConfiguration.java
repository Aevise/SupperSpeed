package pl.Aevise.SupperSpeed.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static Customizer<LogoutConfigurer<HttpSecurity>> logoutConfiguration() {
        return logout -> logout
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizationConfiguration() {
        return auth -> {
            try {
                auth
                        .requestMatchers("/", "/menu/**", "/search/**", "/create/**", "/login", "/logout", "/images/**", "error",
                                "/opinion", "/api/unauth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/client/**").hasAuthority(AvailableRoles.CLIENT.name())
                        .requestMatchers("/restaurant/**", "/upload/**", "/api/auth/restaurant/**").hasAuthority(AvailableRoles.RESTAURANT.name())
                        .requestMatchers("/delete/**", "/orders/**", "/api/auth/orders").hasAnyAuthority(
                                AvailableRoles.CLIENT.name(),
                                AvailableRoles.RESTAURANT.name()
                        );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationConfiguration())
                .httpBasic(Customizer.withDefaults())
//                .oauth2Login(Customizer.withDefaults())
                .formLogin(FormLoginConfigurer::permitAll)
                .logout(logoutConfiguration());
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    SecurityFilterChain securityDisabled(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll());

        return http.build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "test")
    SecurityFilterChain securityTestEnvironment(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorizationConfiguration())
                .httpBasic(Customizer.withDefaults())
                .formLogin(FormLoginConfigurer::permitAll)
                .logout(logoutConfiguration());
        return http.build();
    }
}
