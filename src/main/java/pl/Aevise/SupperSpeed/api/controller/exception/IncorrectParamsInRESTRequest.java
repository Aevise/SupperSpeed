package pl.Aevise.SupperSpeed.api.controller.exception;

public class IncorrectParamsInRESTRequest extends RuntimeException {
    public IncorrectParamsInRESTRequest(String message) {
        super(message);
    }
}
