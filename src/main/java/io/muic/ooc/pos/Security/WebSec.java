package io.muic.ooc.pos.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "http://localhost:3000")
@EnableWebSecurity
public class WebSec extends WebSecurityConfigurerAdapter {

    @Autowired
    private EntryConfig entryConfig;

    @Autowired
    private FailAuth failAuth;

//    @Autowired
//    private SuccessAuth successAuth;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http://stackoverflow.com/questions/19500332/spring-security-and-json-authentication

        http
                .authorizeRequests()
                .antMatchers("/login", "/logout", "/user/register", "/user/add", "/user/checkout", "/user/totalprice", "/user/getRecord", "/user/allRecord", "/user/setStatus").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().loginProcessingUrl("/login")
                .successHandler(new SuccessAuth()).failureHandler(failAuth)
                .and()
                .exceptionHandling().authenticationEntryPoint(entryConfig)
                .and()
                .cors()
                .and()
                .logout().logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll();

        http
                .csrf()
                .disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}