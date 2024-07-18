package pl.Aevise.SupperSpeed.api.controller.exception;

public class IncorrectOrderStatus extends RuntimeException {
    public IncorrectOrderStatus(String message) {
        super(message);
    }
}
