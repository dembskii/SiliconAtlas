package dominik.dembski.lab05.specification;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.dto.CpuSearchCriteriaDTO;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa Specification dla dynamicznego budowania zapytań wyszukiwania CPU.
 * Pozwala na wielokryterialne filtrowanie z wykorzystaniem Spring Data JPA Specification API.
 */
public class CpuSpecification implements Specification<Cpu> {

    private final CpuSearchCriteriaDTO criteria;

    public CpuSpecification(CpuSearchCriteriaDTO criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Cpu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filtrowanie po nazwie modelu (LIKE, case-insensitive)
        if (criteria.getModel() != null && !criteria.getModel().isEmpty()) {
            predicates.add(cb.like(
                cb.lower(root.get("model")),
                "%" + criteria.getModel().toLowerCase() + "%"
            ));
        }

        // Filtrowanie po nazwie producenta (JOIN + LIKE, case-insensitive)
        if (criteria.getManufacturer() != null && !criteria.getManufacturer().isEmpty()) {
            Join<Cpu, Manufacturer> manufacturerJoin = root.join("manufacturer", JoinType.LEFT);
            predicates.add(cb.like(
                cb.lower(manufacturerJoin.get("name")),
                "%" + criteria.getManufacturer().toLowerCase() + "%"
            ));
        }

        // Filtrowanie po minimalnej liczbie rdzeni
        if (criteria.getMinCores() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("cores"), criteria.getMinCores()));
        }

        // Filtrowanie po maksymalnej liczbie rdzeni
        if (criteria.getMaxCores() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("cores"), criteria.getMaxCores()));
        }

        // Filtrowanie po minimalnej liczbie wątków
        if (criteria.getMinThreads() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("threads"), criteria.getMinThreads()));
        }

        // Filtrowanie po maksymalnej liczbie wątków
        if (criteria.getMaxThreads() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("threads"), criteria.getMaxThreads()));
        }

        // Filtrowanie po minimalnej częstotliwości
        if (criteria.getMinFrequency() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("frequencyGhz"), criteria.getMinFrequency()));
        }

        // Filtrowanie po maksymalnej częstotliwości
        if (criteria.getMaxFrequency() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("frequencyGhz"), criteria.getMaxFrequency()));
        }

        // Filtrowanie po nazwie technologii (JOIN + LIKE, case-insensitive)
        if (criteria.getTechnology() != null && !criteria.getTechnology().isEmpty()) {
            Join<Cpu, Technology> technologyJoin = root.join("technologies", JoinType.LEFT);
            predicates.add(cb.like(
                cb.lower(technologyJoin.get("name")),
                "%" + criteria.getTechnology().toLowerCase() + "%"
            ));
            // Distinct aby uniknąć duplikatów przy JOIN z kolekcją
            query.distinct(true);
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Metoda fabryczna do tworzenia Specification z kryteriów.
     */
    public static Specification<Cpu> fromCriteria(CpuSearchCriteriaDTO criteria) {
        return new CpuSpecification(criteria);
    }
}
