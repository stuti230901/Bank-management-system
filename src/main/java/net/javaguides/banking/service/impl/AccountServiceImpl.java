package net.javaguides.banking.service.impl;

import net.javaguides.banking.dto.AccountDTO;
import net.javaguides.banking.dto.TransactionDTO;
import net.javaguides.banking.dto.TransferFundDTO;
import net.javaguides.banking.entity.Account;
import net.javaguides.banking.entity.Transaction;
import net.javaguides.banking.exception.AccountException;
import net.javaguides.banking.mapper.AccountMapper;
import net.javaguides.banking.repository.AccountRepository;
import net.javaguides.banking.repository.TransactionRepository;
import net.javaguides.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public AccountDTO createAccount(AccountDTO accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account saved = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(saved);
    }

    @Override
    public AccountDTO getAccountById(Long Id) {
        return AccountMapper.mapToAccountDto(accountRepository.findById(Id).orElseThrow(() -> new AccountException("Account does not exist")));
    }

    @Override
    public AccountDTO depositMoney(Long id,double depositAmount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        double currentBalance = account.getBalance();
        account.setBalance(currentBalance + depositAmount);
        Account updatedAccount = accountRepository.save(account);

        //Creating transaction object for keeping track of this  operation.
        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(depositAmount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(updatedAccount);
    }

    @Override
    public AccountDTO withdrawMoney(Long id, double withdrawAmount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        double currentBalance = account.getBalance();
        if(currentBalance >= withdrawAmount){
            account.setBalance(currentBalance - withdrawAmount);
        }else{
            throw new RuntimeException("The account does not have sufficient Balance");
        }
        Account updatedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(withdrawAmount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> listOfAccounts = accountRepository.findAll();
        List<AccountDTO> allAccounts = new ArrayList<>();
        for(Account acc:listOfAccounts){
            allAccounts.add(AccountMapper.mapToAccountDto(acc));
        }
        return allAccounts;
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        accountRepository.delete(account);
    }

    @Override
    public void transferFunds(TransferFundDTO transferFundDTO) {
        //Retrieve the account from which we send the fund
        Account fromAccount = accountRepository.findById(transferFundDTO.fromAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));

        //Retrieve the account to which we send the fund
        Account toAccount = accountRepository.findById(transferFundDTO.toAccountId()).orElseThrow(() -> new AccountException("Account does not exist"));

        double toBeDebited = fromAccount.getBalance();
        if (toBeDebited > transferFundDTO.Amount()) {
            fromAccount.setBalance(toBeDebited - transferFundDTO.Amount());
        } else {
            throw new RuntimeException("Balance is insufficient");
        }
        double toBeCredited = toAccount.getBalance();
        toAccount.setBalance(toBeCredited + transferFundDTO.Amount());
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(fromAccount.getId());
        transaction.setAmount(transferFundDTO.Amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

    }

    @Override
    public List<TransactionDTO> getAllTransactions(Long accountId) {
        List<Transaction> listOfTransactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        return listOfTransactions.stream().map((transaction) -> transactionToTransactionDTO(transaction)).collect(Collectors.toList());

    }

    private TransactionDTO transactionToTransactionDTO(Transaction transaction){
        return new TransactionDTO(transaction.getId(),transaction.getAccountId(),transaction.getAmount(),transaction.getTransactionType(),transaction.getTimestamp());
    }

}
