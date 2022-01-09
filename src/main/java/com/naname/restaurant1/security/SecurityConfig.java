package com.naname.restaurant1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        String[] resources = new String[]{
                "/", "/bookings/page/**",
                "/assets/css/**","/assets/fonts/**","/assets/images/**","/assets/js/**",
                };

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(resources).permitAll()
                .anyRequest().authenticated()

                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("login").passwordParameter("pass")
                    .defaultSuccessUrl("/bookings/page/1")
                    .permitAll()

                .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessUrl("/bookings/page/1");
    }
}