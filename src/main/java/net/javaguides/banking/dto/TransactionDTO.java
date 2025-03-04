package net.javaguides.banking.dto;
import java.time.LocalDateTime;

public record TransactionDTO(Long id, Long accountId, double amount, String transactionType, LocalDateTime timestamp) {
}
