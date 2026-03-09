package com.ruralHealth.security.config;


import com.ruralHealth.entity.Role;
import com.ruralHealth.entity.Roles;
import com.ruralHealth.entity.User;
import com.ruralHealth.repository.RolesRepository;
import com.ruralHealth.repository.UserRepository;
import com.ruralHealth.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    // Injecting the DataSource bean, which is typically used for database connectivity in Spring applications,
    // allowing the application to interact with the database for authentication and authorization purposes.
    @Autowired
    DataSource dataSource;

    @Autowired
    private JWTAuthenticationEntryPoint unauthorizedHandler;

    // This method defines a bean for the AuthJWTTokenFilter,
    // which is responsible for processing JWT tokens in incoming requests and authenticating users based on the token's validity.
    @Bean
    public AuthJWTTokenFilter authenticationJwtTokenFilter(){
        return new AuthJWTTokenFilter();
    }

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    // This method defines a bean for the DaoAuthenticationProvider,
    // which is responsible for authenticating users based on data retrieved from a UserDetailsService and encoded passwords.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfigurationBuilder) throws Exception {
        // This method defines a bean for the AuthenticationManager, which is responsible for
        // processing authentication requests in the application.
        // It uses the default implementation provided by Spring Security,
        // allowing for authentication based on the configured UserDetailsService and PasswordEncoder.
        return authenticationConfigurationBuilder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // This method defines a bean for the PasswordEncoder, which is responsible
        // for encoding and verifying passwords in the application.
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

        // Disabling CSRF protection for the application,
        // which is often necessary for stateless applications or APIs that do not use cookies for authentication.
        http.csrf(csrf->csrf.disable());

        // Configuring exception handling to use a custom authentication entry point (AuthEntryPointJwt) for handling unauthorized access attempts,
        // which is typically used in applications that implement JWT-based authentication to provide
        //a consistent response when an unauthenticated user tries to access a protected resource.
        http.exceptionHandling(
                exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
        );

        // Configuring the session management to be stateless,
        // which is common for RESTful APIs that use token-based authentication (like JWT).
        http.sessionManagement(
                session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        //setting up the authorization rules for the application
        http.authorizeHttpRequests((requests)->
                (
                        (AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/patient/**").permitAll()
                                .anyRequest()
                ).authenticated()
        );

        // Configuring HTTP headers to allow the application to be embedded in an iframe from the same origin,
        // which is often necessary for applications that need to be displayed within other web pages or applications.
        http.headers(
                headers-> headers
                        .frameOptions(frameOptions ->frameOptions.sameOrigin() )
        );


        // Adding the custom authentication provider (DaoAuthenticationProvider) to the security configuration,
        // which allows the application to authenticate users based on the data retrieved
        // from the UserDetailsService and the password encoding mechanism defined in the authentication provider.
        http.authenticationProvider(authenticationProvider());

        // Adding a custom authentication filter (AuthTokenFilter) before the default UsernamePasswordAuthenticationFilter in the security filter chain,
        // which is necessary for processing JWT tokens and authenticating users based on the token's validity before the standard authentication process takes place.
        http.addFilterBefore(
                authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return (SecurityFilterChain) http.build();
    }

    // This method is a CommandLineRunner that initializes the application with some default user data.
    @Bean
    public CommandLineRunner initData(RolesRepository roleRepository, UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Roles userRole = roleRepository.findByRoleName(Role.USER)
                    .orElseGet(() -> {
                        Roles newUserRole = new Roles(Role.USER);
                        return roleRepository.save(newUserRole);
                    });


            Roles adminRole = roleRepository.findByRoleName(Role.ADMIN)
                    .orElseGet(() -> {
                        Roles newAdminRole = new Roles(Role.ADMIN);
                        return roleRepository.save(newAdminRole);
                    });
/*
            Roles doctorRole = roleRepository.findByRoleName(Role.DOCTOR)
                    .orElseGet(() -> {
                        Roles newDoctorRole = new Roles(Role.DOCTOR);
                        return roleRepository.save(newDoctorRole);
                    });

            Roles patientRole = roleRepository.findByRoleName(Role.PATIENT)
                    .orElseGet(() -> {
                        Roles newPatientRole = new Roles(Role.PATIENT);
                        return roleRepository.save(newPatientRole);
                    });

            Roles nurseRole = roleRepository.findByRoleName(Role.NURSE)
                    .orElseGet(() -> {
                        Roles newNurseRole = new Roles(Role.NURSE);
                        return roleRepository.save(newNurseRole);
                    });

            Set<Roles> userRoles = Set.of(userRole);
            Set<Roles> doctorRoles = Set.of(doctorRole);
            Set<Roles> patientRoles = Set.of(patientRole);
            Set<Roles> nurseRoles = Set.of(nurseRole);
            Set<Roles> adminRoles = Set.of(userRole, adminRole, doctorRole, patientRole, nurseRole);
*/

            Set<Roles> userRoles = Set.of(userRole);
            Set<Roles> adminRoles = Set.of(userRole, adminRole);

            // Create users if not already present
            if (!userRepository.existsByUserName("user")) {
                com.ruralHealth.entity.User user = new User("user", "user@example.com", passwordEncoder.encode("user123"));
                userRepository.save(user);
            }
/*
            if (!userRepository.existsByUserName("doctor")) {
                com.ruralHealth.entity.User doctor = new User("doctor", "doctor@example.com", passwordEncoder.encode("doctor123"));
                userRepository.save(doctor);
            }

            if (!userRepository.existsByUserName("patient")) {
                com.ruralHealth.entity.User patient = new User("patient", "patient@example.com", passwordEncoder.encode("patient123"));
                userRepository.save(patient);
            }

            if (!userRepository.existsByUserName("nurse")) {
                com.ruralHealth.entity.User nurse = new User("nurse", "nurse@example.com", passwordEncoder.encode("nurse123"));
                userRepository.save(nurse);
            }

*/
            if (!userRepository.existsByUserName("admin")) {
                com.ruralHealth.entity.User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUserName("user").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });
/*
            userRepository.findByUserName("doctor").ifPresent(doctor -> {
                doctor.setRoles(doctorRoles);
                userRepository.save(doctor);
            });

            userRepository.findByUserName("patient").ifPresent(patient -> {
                patient.setRoles(patientRoles);
                userRepository.save(patient);
            });

            userRepository.findByUserName("nurse").ifPresent(nurse -> {
                nurse.setRoles(nurseRoles);
                userRepository.save(nurse);
            });
*/

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


    /*
    *

    @Bean
    public CommandLineRunner initialData(UserDetailsService userDetailsService) {
        return args -> {
            // This method is a CommandLineRunner that initializes the application with some default user data.
            // It creates two users (admin and user) with their respective roles (ADMIN and USER) and saves them to the database using the UserDetailsService.
            JdbcUserDetailsManager jdbcUserDetailsManager = (JdbcUserDetailsManager) userDetailsService();

            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("admin123"))
                    .roles("ADMIN")
                    .build();

            UserDetails user = User.withUsername("user")
                    .password(passwordEncoder().encode( "user123"))
                    .roles("USER")
                    .build();

// The createUser method is called to save the user details to the database, allowing them to be used for authentication and authorization in the application.
            JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
            userDetailsManager.createUser(admin);
            userDetailsManager.createUser(user);
        };
    }
*/




    // to bypass spring security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer (){
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/webjars/**",
                "/configuration/ui",
                "/swagger-resource/**",
                "/configuration/security",
                "/swagger-ui.html"
        ));
    }


}
