package ug.lab04.procesor.exception;

public class CpuNotFoundException extends RuntimeException {
    public CpuNotFoundException(String id) {
        super("CPU not found with id: " + id);
    }
}
