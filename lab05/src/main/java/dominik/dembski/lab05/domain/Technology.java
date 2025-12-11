package dominik.dembski.lab05.domain;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private int releaseYear;

    
    @ManyToMany(mappedBy = "technologies")
    private List<Manufacturer> manufacturers = new ArrayList<>();

    public Technology() {
    }

    public Technology(String name, String description, int releaseYear) {
        this.name = name;
        this.description = description;
        this.releaseYear = releaseYear;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
} 
