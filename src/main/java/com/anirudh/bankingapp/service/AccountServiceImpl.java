package com.anirudh.bankingapp.service;

import com.anirudh.bankingapp.dto.AccountDto;
import com.anirudh.bankingapp.mapper.AccountMapper;
import com.anirudh.bankingapp.model.Account;
import com.anirudh.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository repository;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account= AccountMapper.mapToAccount(accountDto);
        return AccountMapper.mapToAccountDto(repository.save(account));
    }

    @Override
    public AccountDto getAccount(Long id) {
        Account account= repository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public List<AccountDto> getAccounts() {
        List<Account> accounts=repository.findAll();
        return accounts.stream().map((account -> AccountMapper.mapToAccountDto(account))).collect(Collectors.toList());
    }

    @Override
    public AccountDto depositAmount(Long id, double amount) {
        Account account = repository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        double balance=account.getBalance()+amount;
        account.setBalance(balance);
        return AccountMapper.mapToAccountDto(repository.save(account));
    }

    @Override
    public AccountDto withdrawAmount(Long id, double amount) {
        Account account = repository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        double balance=account.getBalance()-amount;
        account.setBalance(balance);
        return AccountMapper.mapToAccountDto(repository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = repository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        repository.delete(account);
    }
}
