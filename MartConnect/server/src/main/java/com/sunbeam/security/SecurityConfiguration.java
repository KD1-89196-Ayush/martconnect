package com.sunbeam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

@Configuration // to declare config class - to declare spring beans - @Bean)
@EnableWebSecurity // to customize spring security
@EnableMethodSecurity // to enable method level annotations
//(@PreAuthorize , @PostAuthorize..) to specify  authorization rules
@AllArgsConstructor
public class SecurityConfiguration {
	//depcy - password encoder
	private final PasswordEncoder encoder;
	private final CustomJwtFilter customJwtFilter;
	private JwtAuthEntryPoint jwtAuthEntryPoint;
/* configure spring bean to customize spring security filter chain
 * disable CSRF protection
 - session creation policy - stateless
 - disable form login based authentication
 - enable basic authentication scheme , for REST clients
 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		//1. Disable CSRF protection
		http.csrf(csrf -> csrf.disable());
		//2. Enable CORS
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		//3. Authenticate any request 
		http.authorizeHttpRequests(request -> 
		//5.permit all - swagger , view all restaurants , user signin , sign up....
		request.requestMatchers("/swagger-ui/**","/v**/api-docs/**",
				"/api/users/login","/api/users/register").permitAll()
		//6. products - GET - to get all products  - no authentication
		.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
		//get product by id - customer
		.requestMatchers(HttpMethod.GET,"/api/products/{id}").permitAll()
		// Categories - allow read access to all, write access to sellers
		.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
		.requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("SELLER")
		.requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("SELLER")
		.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("SELLER")
		// Product management - sellers can manage their products
		.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("SELLER")
		.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("SELLER")
		.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("SELLER")
		// Order management - sellers can view and manage orders
		.requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("SELLER", "CUSTOMER")
		.requestMatchers(HttpMethod.POST, "/api/orders/**").hasRole("CUSTOMER")
		.requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyRole("SELLER", "CUSTOMER")
		// Cart management - customers only
		.requestMatchers("/api/cart/**").hasRole("CUSTOMER")
		// Area management - customers only
		.requestMatchers("/api/areas/**").hasRole("CUSTOMER")
		.anyRequest().authenticated());
		//4. set session creation policy - stateless
		http.sessionManagement(session -> 
		session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		//5. add custom JWT filter before -UserNamePasswordAuthFilter 
		http.addFilterBefore(customJwtFilter
				, UsernamePasswordAuthenticationFilter.class);
		//6. Customize error code of SC 401 , in case of authentication failure
		http.exceptionHandling
		(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint));
		
		System.out.println("Security configuration loaded successfully");
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	//configure a spring to return Spring security authentication manager
	@Bean
	AuthenticationManager authenticationManager
	(AuthenticationConfiguration mgr) throws Exception {
		return mgr.getAuthenticationManager();
	}
	
	
} 