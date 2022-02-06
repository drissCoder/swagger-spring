package com.javainuse;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javainuse.model.AppRole;
import com.javainuse.property.FileStorageProperties;
import com.javainuse.service.AppUserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
@SecurityScheme(name = "tangermed", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Users API", version = "2.0", description = "Users Information"))
@EnableConfigurationProperties(
{ FileStorageProperties.class })
public class SwaggerSpringDemoApplication
{

	public static void main(final String[] args)
	{
		SpringApplication.run(SwaggerSpringDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner start(final AppUserService appUserService)
	{
		return args -> {
			appUserService.save(new AppRole(null, "USER"));
			appUserService.save(new AppRole(null, "ADMIN"));
			Stream.of("admin").forEach(u -> {
				appUserService.saveUser(u, "1234", "1234");
			});
			appUserService.addRoleToUser("admin", "ADMIN");
		};



	}

	@Bean
	BCryptPasswordEncoder getBCPE()
	{
		return new BCryptPasswordEncoder();
	}


}
