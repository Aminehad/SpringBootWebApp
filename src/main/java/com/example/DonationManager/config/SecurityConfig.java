package com.example.DonationManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import com.example.services.UserService;

import lombok.AllArgsConstructor;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private final UserService userService;

    
   @Bean
   public UserDetailsService userDetailsService(){
    return userService;
   }

   @Bean
   public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
   }
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin((form) -> form
               .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
               .defaultSuccessUrl("/products", true)
                .failureUrl("/login?loginError=true"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logoutSuccess=true")
                        .deleteCookies("JSESSIONID"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                (requests) -> {
                    requests.requestMatchers("/login", "/products","/register", "/styles/**", "/pictures/**" ).permitAll();
                    requests.anyRequest().authenticated();
                }
                )
                .cors(Customizer.withDefaults())
                .headers((headers) -> headers.frameOptions(Customizer.withDefaults()).disable());
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

// .csrf(csrf -> 
//                     csrf.ignoringRequestMatchers("/register","/h2-console/**", "/api/**"))