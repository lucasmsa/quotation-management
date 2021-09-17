package com.inatel.quotationmanagement.quotationmanagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inatel.quotationmanagement.quotationmanagement.models.StockQuote;

public interface StockQuotesRepository extends JpaRepository<StockQuote, String> {
    public Optional<StockQuote> findByStockId(String stockId);
}
