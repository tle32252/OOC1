package io.muic.ooc.pos;

//import io.muic.ooc.pos.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Configuration
public class PosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/demo/all").allowedOrigins("http://localhost:3000").allowCredentials(true);
//				registry.addMapping("/demo/order").allowedOrigins("http://localhost:3000").allowCredentials(true);
//				registry.addMapping("/demo/order_to_kitchen").allowedOrigins("http://localhost:3000").allowCredentials(true);
//				registry.addMapping("/test/upload").allowedOrigins("http://localhost:3000").allowCredentials(true);

				registry.addMapping("/**").allowCredentials(true).allowedOrigins("*").allowedMethods("*");
//				registry.addMapping("/**").allowCredentials(true).allowedOrigins("http://localhost").allowedMethods("*");
//				registry.addMapping("/login").allowCredentials(true).allowedOrigins("http://localhost:3000").allowedMethods("*");
			}
		};
	}


	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

}
