package dominik.dembski.lab05.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla odpowiedzi z paginacją.
 * Generyczny typ T pozwala na użycie z różnymi encjami.
 */
@Data
@NoArgsConstructor
public class PagedResponseDTO<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
}
