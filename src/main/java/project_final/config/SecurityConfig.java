package project_final.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD:src/main/java/project_final/config/SecurityConfig.java
import org.springframework.http.HttpStatus;

=======
>>>>>>> origin/master:src/main/java/project_final/config/WebSecurityConfig.java
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
<<<<<<< HEAD:src/main/java/project_final/config/SecurityConfig.java
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
=======
>>>>>>> origin/master:src/main/java/project_final/config/WebSecurityConfig.java
import project_final.security.UserDetailService;

import javax.servlet.http.Cookie;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
<<<<<<< HEAD:src/main/java/project_final/config/SecurityConfig.java
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
=======
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
>>>>>>> origin/master:src/main/java/project_final/config/WebSecurityConfig.java
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
<<<<<<< HEAD:src/main/java/project_final/config/SecurityConfig.java
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
=======
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/**/").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/403/**").permitAll()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/home/sign-in")
//                .loginProcessingUrl("/auth/sign-in")
//                .defaultSuccessUrl("/home")
>>>>>>> origin/master:src/main/java/project_final/config/WebSecurityConfig.java
                .and()
                .formLogin()
                .loginPage("/public/login")
                .loginProcessingUrl("/public/login")
                .defaultSuccessUrl("/home")
                .failureForwardUrl("/error")
        ;
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home")
                .permitAll();


        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
//
        http.httpBasic()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedPage("/403");
    }
}
