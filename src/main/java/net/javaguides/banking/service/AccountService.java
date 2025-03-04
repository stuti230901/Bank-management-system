package net.javaguides.banking.service;

import net.javaguides.banking.dto.AccountDTO;
import net.javaguides.banking.dto.TransactionDTO;
import net.javaguides.banking.dto.TransferFundDTO;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDto);

    AccountDTO getAccountById(Long Id);

    AccountDTO depositMoney(Long id,double depositAmount);

    AccountDTO withdrawMoney(Long id, double withdrawAmount);

    List<AccountDTO> getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferFundDTO transferFundDTO);

    List<TransactionDTO> getAllTransactions(Long accountId);
}
