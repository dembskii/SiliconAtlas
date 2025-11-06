package com.ug.CsvApp.office;

import com.ug.CsvApp.domain.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class OfficeConfig {

    @Bean
    public Person prezes() {
        return new Person(UUID.randomUUID(), "Jan", "Kowalski", "prezes@example.com", 1970);
    }

    @Bean
    public Person wiceprezes() {
        return new Person(UUID.randomUUID(), "Anna", "Nowak", "wiceprezes@example.com", 1975);
    }

    @Bean
    public Person sekretarz() {
        return new Person(UUID.randomUUID(), "Piotr", "Zieliński", "sekretarz@example.com", 1980);
    }
}