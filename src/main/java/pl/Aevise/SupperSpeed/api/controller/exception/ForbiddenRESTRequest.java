package pl.Aevise.SupperSpeed.api.controller.exception;

public class ForbiddenRESTRequest extends RuntimeException {
    public ForbiddenRESTRequest(String message) {
        super(message);
    }
}
