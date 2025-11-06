package com.ug.CsvApp;

import com.ug.CsvApp.domain.Person;
import com.ug.CsvApp.service.CsvPersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class CsvAppApplication implements CommandLineRunner {

    private final CsvPersonService csvPersonService;

    public CsvAppApplication(CsvPersonService csvPersonService) {
        this.csvPersonService = csvPersonService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CsvAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            List<Person> people = csvPersonService.readPeople();
            System.out.println("Wczytano " + people.size() + " osób:");
            people.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
