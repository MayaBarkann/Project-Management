package projectManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import projectManagement.filter.PermissionFilter;
import projectManagement.filter.TokenFilter;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("*").authenticated().and().httpBasic().and().csrf().disable();
//        http.addFilterAfter(new TokenFilter(), BasicAuthenticationFilter.class);
//        http.addFilterAfter(new PermissionFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }
}