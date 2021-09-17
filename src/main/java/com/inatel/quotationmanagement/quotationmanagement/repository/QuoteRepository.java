package com.inatel.quotationmanagement.quotationmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inatel.quotationmanagement.quotationmanagement.models.Quote;

public interface QuoteRepository extends JpaRepository<Quote, String> {}