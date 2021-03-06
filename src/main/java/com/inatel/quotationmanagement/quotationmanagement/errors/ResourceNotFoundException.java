package com.inatel.quotationmanagement.quotationmanagement.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Stock could not be found")
public class ResourceNotFoundException extends RuntimeException {}
