package com.inatel.quotationmanagement.quotationmanagement.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource already exists")
public class ResourceAlreadyExists extends RuntimeException {}
