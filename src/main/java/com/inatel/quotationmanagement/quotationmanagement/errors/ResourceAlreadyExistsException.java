package com.inatel.quotationmanagement.quotationmanagement.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Stock already exists")
public class ResourceAlreadyExistsException extends RuntimeException {}
