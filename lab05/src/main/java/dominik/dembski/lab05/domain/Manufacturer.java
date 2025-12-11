package dominik.dembski.lab05.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Entity
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String country;
    private int foundedYear;

    
    @OneToMany(mappedBy = "manufacturer")
    private List<Cpu> cpus = new ArrayList<>();

    
    @ManyToMany
    @JoinTable(
        name = "manufacturer_technology",
        joinColumns = @JoinColumn(name = "manufacturer_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies = new ArrayList<>();

    public Manufacturer() {
    }

    public Manufacturer(String name, String country, int foundedYear) {
        this.name = name;
        this.country = country;
        this.foundedYear = foundedYear;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public List<Cpu> getCpus() {
        return cpus;
    }

    public void setCpus(List<Cpu> cpus) {
        this.cpus = cpus;
    }

    public void addCpu(Cpu cpu) {
        cpus.add(cpu);
        cpu.setManufacturer(this);
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    public void addTechnology(Technology technology) {
        technologies.add(technology);
        technology.getManufacturers().add(this);
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", foundedYear=" + foundedYear +
                '}';
    }
}