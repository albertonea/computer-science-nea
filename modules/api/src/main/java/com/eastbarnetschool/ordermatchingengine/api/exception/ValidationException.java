package com.eastbarnetschool.ordermatchingengine.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;

import static com.eastbarnetschool.ordermatchingengine.api.exception.ProblemDetailExt.forStatusDetailAndErrors;

public class ValidationException extends ErrorResponseException {

    public ValidationException(final HttpStatus status, final Map<String, String> errors) {
        super(status, forStatusDetailAndErrors(status, "Request validation failed", errors), null);
    }

}