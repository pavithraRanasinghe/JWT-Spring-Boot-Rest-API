package lk.example.springsecurity.security;

import lk.example.springsecurity.jwt.JwtAuthenticationFilter;
import lk.example.springsecurity.jwt.JwtAuthorizationFilter;
import lk.example.springsecurity.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {


    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(),jwtConfig))
                .addFilterAfter(new JwtAuthorizationFilter(jwtConfig), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/users/sign-up").permitAll()
                .anyRequest()
                .authenticated();
    }
}
