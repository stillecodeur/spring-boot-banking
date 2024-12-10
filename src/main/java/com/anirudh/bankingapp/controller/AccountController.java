package com.anirudh.bankingapp.controller;

import com.anirudh.bankingapp.dto.AccountDto;
import com.anirudh.bankingapp.dto.TransferFundDto;
import com.anirudh.bankingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("accounts/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody  AccountDto accountDto) {
        AccountDto newAccountDto = accountService.createAccount(accountDto);
        return new ResponseEntity<>(newAccountDto, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) {
        AccountDto newAccountDto = accountService.getAccount(id);
        return new ResponseEntity<>(newAccountDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<AccountDto> newAccountDtoList = accountService.getAccounts();
        return ResponseEntity.ok(newAccountDtoList);
    }

    @PutMapping("{id}/deposit")
    public ResponseEntity<AccountDto> depositAmount(@PathVariable Long id, @RequestBody  Map<String,Double> request) {
        AccountDto newAccountDto = accountService.depositAmount(id, request.get("amount"));
        return ResponseEntity.ok(newAccountDto);
    }

    @PutMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdrawAmount(@PathVariable Long id, @RequestBody  Map<String,Double> request) {
        AccountDto newAccountDto = accountService.withdrawAmount(id, request.get("amount"));
        return ResponseEntity.ok(newAccountDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @PostMapping("transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);
        return ResponseEntity.ok("Transfer successfully");
    }

}
