package ru.petproject.socialnetwork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import ru.petproject.socialnetwork.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private CsrfHeaderFilter csrfFilter;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, CsrfHeaderFilter csrfFilter) {
        this.userDetailsService = userDetailsService;
        this.csrfFilter = csrfFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**/*.js")
                .antMatchers("/**/*.ico")
                .antMatchers("/**/*.css")
                .antMatchers("/**/*.html")
                .antMatchers("/bootstrap/**")
                .antMatchers("/" + Constants.AVATAR_FOLDER + "anonymous.png")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final String[] swagger = {
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/v2/api-docs",
                "/webjars/**"
        };

        // @formatter:off
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(swagger).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/signUp").permitAll()//Redundant
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                .deleteCookies(Constants.REMEMBER_ME_COOKIE)
                .invalidateHttpSession(true)
                .logoutUrl("/api/logout")
                .permitAll()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeService())
                .key(Constants.REMEMBER_ME_TOKEN)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Access Denied"))
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .cors()
                .and()
                .addFilterAfter(csrfFilter, CsrfFilter.class)
        ;
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(Constants.XSRF_TOKEN_HEADER_NAME);
        return repository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeService() {
        final TokenBasedRememberMeServices services =
                new TokenBasedRememberMeServices(Constants.REMEMBER_ME_TOKEN, userDetailsService);

        services.setCookieName(Constants.REMEMBER_ME_COOKIE);
        services.setTokenValiditySeconds(3600);
        services.setAlwaysRemember(true);

        return services;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("OPTIONS");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}