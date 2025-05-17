package com.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.service.CustomUserDetialsService;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;


@Configuration
public class ApplicationConfig {
	@Autowired
	private CustomUserDetialsService userDetailsService;

	
	@Bean
    PasswordEncoder passwordEncoder () {
    	return new BCryptPasswordEncoder();
    }
  
 
  @Bean
  AuthenticationProvider authenticationProvider() {
	  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	  authProvider.setUserDetailsService(userDetailsService);
	  authProvider.setPasswordEncoder(passwordEncoder());
	  
	  return authProvider;
  }
  @Bean
  public OpenAPI customOpenApiConfig() {
	  final String securitySchemeName= "bearerAuth";
	  return new OpenAPI().info(new Info()
			  .title("MyStudyApp API Documentation")
			  .description("This is an API documentation for a group chat that promotes studying between users")
			  .version("1.0")
			  .contact(new Contact().email("jedidiahsylvanus@gmail.com").name("Sylvanus Jedidiah"))
			  )
			  .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
			  .components(new Components()
					  .addSecuritySchemes(securitySchemeName, new SecurityScheme()
							  .name(securitySchemeName)
							  .type(Type.HTTP)
							  .scheme("Bearer")
							  .bearerFormat("JWT")));
  }
}
