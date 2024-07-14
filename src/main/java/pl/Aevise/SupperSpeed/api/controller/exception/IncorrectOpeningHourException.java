package pl.Aevise.SupperSpeed.api.controller.exception;

public class IncorrectOpeningHourException extends RuntimeException {
    public IncorrectOpeningHourException(String message) {
        super(message);
    }
}
