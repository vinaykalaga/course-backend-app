            package com.course.app.security;

            import com.course.app.config.CorsGlobalConfig;
            import com.course.app.service.UserService;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.http.HttpMethod;
            import org.springframework.security.authentication.AuthenticationManager;
            import org.springframework.security.authentication.AuthenticationProvider;
            import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
            import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
            import org.springframework.security.config.annotation.web.builders.HttpSecurity;
            import org.springframework.security.config.http.SessionCreationPolicy;
            import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
            import org.springframework.security.crypto.password.PasswordEncoder;
            import org.springframework.security.web.SecurityFilterChain;
            import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

            @Configuration
            public class SecurityConfig {

                private final JwtFilter jwtFilter;

                public SecurityConfig(JwtFilter jwtFilter) {
                    this.jwtFilter = jwtFilter;
                }


                @Bean
                public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                    http
                            .csrf(csrf -> csrf.disable())
                            //.cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ Use the CORS config bean
                            .cors().configurationSource(new CorsGlobalConfig().corsConfigurationSource())
                            .and()
                            .authorizeHttpRequests(auth -> auth
                                    // 🔓 Publicly accessible endpoints
                                    .requestMatchers(
                                            "/auth/**",
                                            "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html"
                                    ).permitAll()

                                    // 📚 Public GETs (viewing available courses)
                                    .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()

                                    // 👨‍🎓 Learner-only routes
                                    .requestMatchers(HttpMethod.GET, "/courses/status/**").hasAnyRole("LEARNER","INSTRUCTOR")
                                    .requestMatchers(HttpMethod.POST, "/courses/enroll/**").hasRole("LEARNER")
                                    .requestMatchers(HttpMethod.POST, "/courses/complete/**").hasRole("LEARNER")
                                    .requestMatchers(HttpMethod.GET, "/courses/my-courses").hasRole("LEARNER")
                                    .requestMatchers(HttpMethod.GET, "/certificate/download/**").hasRole("LEARNER")

                                    // 🧑‍🏫 Instructor-only actions
                                    //.requestMatchers(HttpMethod.GET, "/courses/status/**").hasRole("INSTRUCTOR")
                                    .requestMatchers(HttpMethod.POST, "/courses/**").hasRole("INSTRUCTOR")
                                    .requestMatchers(HttpMethod.PUT, "/courses/**").hasRole("INSTRUCTOR")
                                    .requestMatchers(HttpMethod.DELETE, "/courses/**").hasRole("INSTRUCTOR")
                                    .requestMatchers(HttpMethod.GET, "/instructor/**").hasRole("INSTRUCTOR")
                                    .requestMatchers(HttpMethod.GET, "/courses/instructor/**").hasRole("INSTRUCTOR")

                                    // 🛡️ All other requests require authentication
                                    .anyRequest().authenticated()
                            )
                            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                    return http.build();
                }


                @Bean
                public AuthenticationProvider authenticationProvider(UserService userService) {
                    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                    provider.setUserDetailsService(userService);
                    provider.setPasswordEncoder(passwordEncoder());
                    return provider;
                }

                @Bean
                public PasswordEncoder passwordEncoder() {
                    return new BCryptPasswordEncoder();
                }

                @Bean
                public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                    return config.getAuthenticationManager();
                }
            }
