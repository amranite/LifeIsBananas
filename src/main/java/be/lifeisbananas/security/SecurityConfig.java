package be.lifeisbananas.security;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsManager authenticateUsers() {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery(
                "select email, password, true as enabled from persons where email=?");
        users.setAuthoritiesByUsernameQuery(
                "select persons.email, roles.role_name as authority " +
                "from persons, roles where persons.id=roles.person_id and persons.email=?");
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/home.html", "/index.html").permitAll()
                        .requestMatchers("/login", "/login.html").permitAll()
                        .requestMatchers("/logout", "/logoutSuccess.html").permitAll()
                        .requestMatchers("/accessDenied", "/accessDenied.html", "/403.html").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/bio-engineer/**").hasAuthority("BIO_ENGINEER")
                        .requestMatchers("/warehouse/**").hasAuthority("WAREHOUSE_MANAGER")
                        .requestMatchers("/raw-materials/new", "/raw-materials", "/raw-materials/*/edit",
                                "/raw-materials/*", "/raw-materials/*/delete").authenticated()
                        .requestMatchers("/raw-materials/**").authenticated()
                        .requestMatchers("/menu.html", "/role.html").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu.html", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logoutSuccess.html"))
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler((request, response, ex) -> response.sendRedirect("/403.html")))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
