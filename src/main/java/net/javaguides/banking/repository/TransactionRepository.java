package net.javaguides.banking.repository;

import net.javaguides.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //This is the method which implements the query
    //FindByAccountId tells the spring JPA to create a query to find the transactions by Account Ids.
    //Then the orderbytimestamp tell that they should be ordered by time stamps
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}
