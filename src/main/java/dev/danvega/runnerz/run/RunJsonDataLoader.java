package dev.danvega.runnerz.run;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RunJsonDataLoader implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RunJsonDataLoader.class);
	private RunRepository runRepository;
	private ObjectMapper objectMapper;
	
	public RunJsonDataLoader(RunRepository runRepository, ObjectMapper objectMapper) {
		this.runRepository = runRepository;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (runRepository.count() == 0)
		{
			try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json"))
			{
				Runs runs = objectMapper.readValue(inputStream, Runs.class);
				log.info("Read json data");
				runRepository.saveAll(runs.runs());
				
			}
			catch (IOException e)
			{
				throw new RuntimeException ("Failed to read JSON data", e);
			}			
		}
		else
		{
			log.info("Not loading Runs from JSON data because the collections contains data");
		}
		
	}

}
