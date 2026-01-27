package dominik.dembski.lab05.domain;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "cpus")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private int releaseYear;

    @ManyToMany(mappedBy = "technologies")
    @JsonIgnore
    private List<Cpu> cpus;

    public Technology(String name, String description, int releaseYear) {
        this.name = name;
        this.description = description;
        this.releaseYear = releaseYear;
    }
} 
