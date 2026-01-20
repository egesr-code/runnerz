package dev.danvega.runnerz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Runnerz Spring Boot application.
 * This application provides a REST API for tracking running activities.
 *
 * @author Dan Vega
 */
@SpringBootApplication
public class Application {

	// PRUEBA 2 PARA HACER UN PULL REQUEST
	private static final Logger log = LoggerFactory.getLogger(Application.class);


	/**
	 * Main method that bootstraps the Spring Boot application.
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		//System.setProperty("spring.devtools.restart.enabled", "false");
		
		SpringApplication.run(Application.class, args);
		log.info("Apps started succesfully");
	}
	
//	@Bean
//	CommandLineRunner runner (RunRepository runRepository)
//	{
//		return args -> {
//			Run run = new Run(1, "FirstRun", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.OUTDOOR);
//			runRepository.create(run);
//			log.info("Run: " + run);
//		};
//	}

}
