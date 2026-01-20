package dev.danvega.runnerz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	
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
