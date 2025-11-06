package com.ug.CsvApp.service;

import com.ug.CsvApp.domain.Person;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CsvPersonService {

    public List<Person> readPeopleFromClasspath(String resourceName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new FileNotFoundException(resourceName + " not found in classpath");
            }

            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .parse(reader);

                for (CSVRecord record : parser) {
                    String idStr = getField(record, "id");
                    if (idStr.isEmpty()) continue; // pomiń rekordy bez id

                    UUID id = UUID.fromString(idStr);
                    String firstName = getField(record, "name", "firstName");
                    String lastName = getField(record, "surname", "lastName");
                    String email = getField(record, "email");
                    String yearStr = getField(record, "year_of_birth", "yearOfBirth");
                    int year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);

                    people.add(new Person(id, firstName, lastName, email, year));
                }
            }
        }

        return people;
    }

    
    public List<Person> readPeople() throws IOException {
        return readPeopleFromClasspath("fake_people.csv");
    }

    // helper: zwraca pierwszą dopasowaną nazwę kolumny z podanych alternatyw
    private String getField(CSVRecord record, String... names) {
        for (String n : names) {
            if (record.isMapped(n)) {
                String v = record.get(n);
                if (v != null) return v.trim();
            }
        }
        return "";
    }
}
