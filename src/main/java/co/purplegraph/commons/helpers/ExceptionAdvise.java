package co.purplegraph.commons.helpers;

import co.purplegraph.commons.exceptions.PurpleUserException;
import co.purplegraph.commons.params.ApiResponse;
import co.purplegraph.commons.params.PurpleContext;
import co.purplegraph.commons.services.ContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;

import static co.purplegraph.commons.utils.ConstantsKt.GENERAL_FAILED_MESSAGE;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvise {

    private final ContextService contextService;

    @ExceptionHandler(value = PurpleUserException.class)
    public ResponseEntity<?> handlePurpleUserException(PurpleUserException apiException) {
        PurpleContext ctx = contextService.getContextFromRequest();
        log.error("{} - {}", ctx.getTenant(), ctx.getUsername(), apiException);
        return new ResponseEntity<>(ApiResponse.Companion.error(apiException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentException(MethodArgumentNotValidException exception) {

        ArrayList<Object> errors = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            HashMap<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("error", fieldError.getDefaultMessage());
            errors.add(error);
        }
        return new ResponseEntity<>(ApiResponse.Companion.errorWithFields("Invalid request payload", errors), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handlePurpleInternalException(Exception apiException) {
        PurpleContext ctx = contextService.getContextFromRequest();
        log.error("{} - {}", ctx.getTenant(), ctx.getUsername(), apiException);
        return new ResponseEntity<>(ApiResponse.Companion.error(GENERAL_FAILED_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
