package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                
        http.csrf().disable();
        
        
        http
        .authorizeRequests()
            // Vulnerability #2 2013-A2-Broken Authentication and Session Management.
            // A custom logout page is "defined" but it does not invalidate the
            // session ID.

            // Q: How to fix this?
            // A1: Use default logout page by 
            // removing next line and fix logout url in templates (form.html) 
            // A2: Configure custom logout according to
            // https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#jc-logout
            .antMatchers("/custom_logout").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .permitAll()
            .and()
        .logout()
            .permitAll();
        
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
