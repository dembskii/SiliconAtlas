package com.ug.CsvApp.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.ug.CsvApp.domain.Person;



public class PeopleUtils {

    private static List<Person> readPeopleFromClasspath(String resourceName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (InputStream is = PeopleUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
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
                    String firstName = PeopleUtils.getField(record, "name", "firstName");
                    String lastName = PeopleUtils.getField(record, "surname", "lastName");
                    String email = PeopleUtils.getField(record, "email");
                    String yearStr = PeopleUtils.getField(record, "year_of_birth", "yearOfBirth");
                    int year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);

                    people.add(new Person(id, firstName, lastName, email, year));
                }
            }
        }

        return people;
    }

    
    public static List<Person> readPeople() {
        List<Person> people = new ArrayList<>();
        try {
            people = readPeopleFromClasspath("fake_people.csv");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return people;
    }

    // helper: zwraca pierwszą dopasowaną nazwę kolumny z podanych alternatyw
    private static String getField(CSVRecord record, String... names) {
        for (String n : names) {
            if (record.isMapped(n)) {
                String v = record.get(n);
                if (v != null) return v.trim();
            }
        }
        return "";
    }
    
}
