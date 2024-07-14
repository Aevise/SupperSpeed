package pl.Aevise.SupperSpeed.api.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.domain.exception.ProcessingException;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        String message = String.format("Other exception occurred: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView handleNoOrdersFound(
            InvalidDataAccessResourceUsageException exception,
            HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String message = String.format("Queried data not found: [%s]", exception.getMessage());
        log.warn(message);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:" + referer);
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoResourceFound(NotFoundException ex) {
        String message = String.format("Could not find a resource: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(ProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(ProcessingException ex) {
        String message = String.format("Processing exception occurred: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(BindException ex) {
        String message = String.format("Bad request for field: [%s], wrong value: [%s]",
                Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null),
                Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null));
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView handleException(AccessDeniedException ex) {
        String message = String.format("Error:\n[%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleException(UserNotFoundException ex) {
        String message = String.format("Error:\n[%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(IncorrectOpeningHourException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleException(IncorrectOpeningHourException ex) {
        String message = String.format("Error:\n[%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(IncorrectOrderStatus.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView handleException(IncorrectOrderStatus ex) {
        String message = String.format("Error:\n[%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }
}
