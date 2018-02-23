package com.snorlacs.newse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static com.snorlacs.newse.configuration.AuthenticationEntryPoint.REALM_NAME;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:../resources/application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${editor.username}")
    private String username;

    @Value("${editor.password}")
    private String password;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/article").authenticated()
                .antMatchers(HttpMethod.POST, "/article").hasRole("EDITOR")
                .antMatchers(HttpMethod.PUT, "/article/*").hasRole("EDITOR")
                .antMatchers(HttpMethod.DELETE, "/article/*").hasRole("EDITOR")
                .antMatchers(HttpMethod.GET, "/article/*").permitAll()
                .and().httpBasic().realmName(REALM_NAME).authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication().withUser(username).password(password).roles("EDITOR");
        }
}


