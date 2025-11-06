package com.ug.CsvApp.office;

import com.ug.CsvApp.domain.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfficeService  {

    private final Person prezes;
    private final Person wiceprezes;
    private final Person sekretarz;
    private final List<Person> allPersons;

    public OfficeService(@Qualifier("prezes") Person prezes,
                         @Qualifier("wiceprezes") Person wiceprezes,
                         @Qualifier("sekretarz") Person sekretarz,
                         @Qualifier("pracownicy") List<Person> allPersons) {
        this.prezes = prezes;
        this.wiceprezes = wiceprezes;
        this.sekretarz = sekretarz;
        this.allPersons = allPersons;
    }

    public List<Person> getAllPersons() {
        return this.allPersons;
    }


    

    // @Override
    // public void run(String... args) {
    //     System.out.println("");
    //     System.out.println("Wstrzyknięte role (beans z @Configuration):");
    //     System.out.println("prezes -> " + prezes);
    //     System.out.println("wiceprezes -> " + wiceprezes);
    //     System.out.println("sekretarz -> " + sekretarz);

    //     System.out.println("\nWszystkie beany typu Person (w tym z beans.xml):");
    //     allPersons.forEach(p -> System.out.println(" - " + p));
    // }
}