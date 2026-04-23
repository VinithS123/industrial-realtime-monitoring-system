package com.example.PRISM;

import com.example.PRISM.entity.MachineEntity;
import com.example.PRISM.repository.MachineRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableScheduling
public class PrismApplication {

	public static void main(String[] args) {

		SpringApplication.run(PrismApplication.class, args);
	}

	// Initialize the 4 Specific Machines on Startup
	@Bean
	CommandLineRunner init(MachineRepository repo) {
		return args -> {
			// Only initialize if the database is empty
			if (repo.count() == 0) {
				System.out.println("Initializing Industrial Database...");

				// Chemical Reactor
				repo.save(new MachineEntity(null, "Chemical Reactor", "Reactor", "R-001","normal", "Building A"));

				// Biotech Fermenter
				repo.save(new MachineEntity(null, "Biotech Fermenter", "Fermenter","F-003", "normal", "Building B"));

				// Distillation Column
				repo.save(new MachineEntity(null, "Distillation Column", "Distillation", "D-002", "normal", "Building C"));

				// Heat Exchanger
				repo.save(new MachineEntity(null, "Heat Exchanger", "Exchanger", "HX-005", "normal", "Building D"));

				System.out.println("Database initialized with "+repo.count()+" industrial units.");
			}
		};
	}
}