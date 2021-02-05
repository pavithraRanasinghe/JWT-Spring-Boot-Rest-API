package lk.example.springsecurity.security;

import lk.example.springsecurity.jwt.JwtAuthenticationFilter;
import lk.example.springsecurity.jwt.JwtAuthorizationFilter;
import lk.example.springsecurity.jwt.JwtConfig;
import lk.example.springsecurity.oauth.CustomOAuth2UserService;
import lk.example.springsecurity.oauth.OAuth2LoginSuccessfulHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;
    @Autowired
    private OAuth2LoginSuccessfulHandler oAuth2LoginSuccessfulHandler;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     JwtConfig jwtConfig) {
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
                .antMatchers(jwtConfig.getUrl()).permitAll()
                .antMatchers("/users/view").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/forget/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                    .loginPage(jwtConfig.getUrl())
                    .userInfoEndpoint().userService(oAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessfulHandler);
    }
}
