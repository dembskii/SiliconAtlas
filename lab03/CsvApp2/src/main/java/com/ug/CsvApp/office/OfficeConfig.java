package com.ug.CsvApp.office;

import com.ug.CsvApp.domain.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ug.CsvApp.utils.PeopleUtils;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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

    @Bean
    public List<Person> pracownicy() {
        System.out.println("Ładowanie listy osób z CSV w OfficeConfig...");
        List<Person> people = PeopleUtils.readPeople();
        people.forEach(p -> System.out.println(" - " + p));

        return people;
    }
}