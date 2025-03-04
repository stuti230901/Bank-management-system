package net.javaguides.banking.controller;

import net.javaguides.banking.dto.AccountDTO;
import net.javaguides.banking.dto.TransactionDTO;
import net.javaguides.banking.dto.TransferFundDTO;
import net.javaguides.banking.entity.Account;
import net.javaguides.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    //Add Account REST API
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    //Get Account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id){
        AccountDTO accountDTO = accountService.getAccountById(id);
        if(accountDTO != null){
            return new ResponseEntity<>(accountDTO,HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Deposit Balance in Account
    @PutMapping("/deposit/{id}")
    public ResponseEntity<AccountDTO> depositBalanceById(@PathVariable Long id, @RequestBody Map<String, Double> request){
        AccountDTO accountDTO = accountService.getAccountById(id);
        if(accountDTO !=null){
            double depositAmount = request.get("amount");
            return new ResponseEntity<>(accountService.depositMoney(id,depositAmount),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Withdraw money from account
    @PutMapping("/withdraw/{id}")
    public ResponseEntity<AccountDTO> withdrawBalanceById(@PathVariable Long id, @RequestBody Map<String,Double> request){
        AccountDTO accountDTO = accountService.getAccountById(id);
        if(accountDTO != null){
            double currentBalance = request.get("amount");
            return new ResponseEntity<>(accountService.withdrawMoney(id,currentBalance),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get the list of all accounts
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts,HttpStatus.OK);
    }

    //Delete Account by Id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccountById(@PathVariable Long id){
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account is deleted successfully!",HttpStatus.OK);
    }

    //Transfer fund from one Account to another
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDTO transferFundDTO){
        accountService.transferFunds(transferFundDTO);
        return new ResponseEntity<>("Transfer Successful!",HttpStatus.OK);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForAccountById(@PathVariable Long id){
        List<TransactionDTO> transactionDTOList = accountService.getAllTransactions(id);
        return new ResponseEntity<>(transactionDTOList,HttpStatus.OK);
    }


}
