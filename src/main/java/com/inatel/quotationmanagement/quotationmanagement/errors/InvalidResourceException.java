package com.inatel.quotationmanagement.quotationmanagement.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One or more of your dates are invalid or the date you're trying to add was already added")
public class InvalidResourceException extends RuntimeException {}
