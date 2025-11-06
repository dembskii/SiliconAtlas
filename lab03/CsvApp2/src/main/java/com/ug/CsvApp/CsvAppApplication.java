package com.ug.CsvApp;

import com.ug.CsvApp.domain.Person;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import com.ug.CsvApp.office.OfficeService;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class CsvAppApplication  {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CsvAppApplication.class, args);
        List<Person> people = context.getBean("pracownicy", List.class);
        // List<Person> people = csvPersonService.readPeople();
        System.out.println("######### " +  " lista osób z kontekstu apliakcji:");
        System.out.println("Wczytano " + people.size() + " osób:");
        people.forEach(System.out::println);
        OfficeService officeService = context.getBean(OfficeService.class);
        System.out.println("######### " +  " lista osób z beana office service:");
        officeService.getAllPersons().forEach(System.out::println);
        Person adam = context.getBean("xmlPersonAdam", Person.class);
        System.out.println(adam);
    }
}
