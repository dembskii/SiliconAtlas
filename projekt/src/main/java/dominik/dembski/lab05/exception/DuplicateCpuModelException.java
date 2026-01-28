package dominik.dembski.lab05.exception;

/**
 * Wyjątek rzucany przy próbie dodania CPU z modelem, który już istnieje w bazie.
 */
public class DuplicateCpuModelException extends RuntimeException {
    
    private final String model;
    
    public DuplicateCpuModelException(String model) {
        super("CPU o modelu '" + model + "' już istnieje w bazie danych");
        this.model = model;
    }
    
    public String getModel() {
        return model;
    }
}
