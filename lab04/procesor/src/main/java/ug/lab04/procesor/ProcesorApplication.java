package ug.lab04.procesor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ug.lab04.procesor.controller.CpuController;
import ug.lab04.procesor.cpu.Cpu;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ProcesorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcesorApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(CpuController cpuController) {
		return args -> {
			// Inicjalizacja bazy danych z kilkoma przykładowymi procesorami
			List<Cpu> initialCpus = Arrays.asList(
					new Cpu("Intel Core i9-13900K", 24, 32, 5.8),
					new Cpu("AMD Ryzen 9 7950X", 16, 32, 5.7),
					new Cpu("Intel Core i7-13700K", 16, 24, 5.4),
					new Cpu("AMD Ryzen 7 7700X", 8, 16, 5.4),
					new Cpu("Intel Core i5-13600K", 14, 20, 5.1)
			);
			
			cpuController.initializeDatabase(initialCpus);
			
			System.out.println("✅ Database initialized with " + initialCpus.size() + " CPUs");
			initialCpus.forEach(cpu -> 
				System.out.println("  - " + cpu.getProducer() + " (ID: " + cpu.getId() + ")")
			);
		};
	}

}
