package net.javaguides.banking.mapper;

import net.javaguides.banking.dto.AccountDTO;
import net.javaguides.banking.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDTO accountDTO){
        return new Account(
                accountDTO.getId(),
                accountDTO.getAccountHolderName(),
                accountDTO.getBalance()
        );
    }

    public static AccountDTO mapToAccountDto(Account account){
        return new AccountDTO(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
    }
}
