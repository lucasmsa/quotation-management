package com.inatel.quotationmanagement.quotationmanagement.validation;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

@Service
public class QuoteDateValidator {

    public QuoteDateValidator() {}

    public boolean isValid(String dateStr) {
        return GenericValidator.isDate(dateStr, "yyyy-MM-dd", true);
    }
}
