package com.anirudh.bankingapp.service;

import com.anirudh.bankingapp.dto.AccountDto;
import com.anirudh.bankingapp.dto.TransactionDto;
import com.anirudh.bankingapp.dto.TransferFundDto;
import com.anirudh.bankingapp.exception.AccountException;
import com.anirudh.bankingapp.mapper.AccountMapper;
import com.anirudh.bankingapp.model.Account;
import com.anirudh.bankingapp.model.Transaction;
import com.anirudh.bankingapp.model.TransactionType;
import com.anirudh.bankingapp.repository.AccountRepository;
import com.anirudh.bankingapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public AccountDto getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public List<AccountDto> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account -> AccountMapper.mapToAccountDto(account))).collect(Collectors.toList());
    }

    @Override
    public AccountDto depositAmount(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        double balance = account.getBalance() + amount;
        account.setBalance(balance);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setTransactionType(TransactionType.DEPOSIT.getTransactionType());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public AccountDto withdrawAmount(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        double balance = account.getBalance() - amount;
        account.setBalance(balance);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setTransactionType(TransactionType.WITHDRAW.getTransactionType());
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        accountRepository.delete(account);
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        Account fromAccount = accountRepository.findById(transferFundDto.fromAccountId()).orElseThrow(() -> new AccountException("Account doesn't exist"));
        Account toAccount = accountRepository.findById(transferFundDto.toAccountId()).orElseThrow(() -> new AccountException("Account doesn't exist"));

        if (fromAccount.getBalance() < transferFundDto.amount()) {
            throw new RuntimeException("Insufficient amount");
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDto.fromAccountId());
        transaction.setTransactionType(TransactionType.TRANSFER.getTransactionType());
        transaction.setAmount(transferFundDto.amount());
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long id) {
        List<Transaction> transactions=transactionRepository.findByAccountIdOrderByTimestampDesc(id);
        return transactions.stream().map((transaction -> convertEntityToDto(transaction))).collect(
                Collectors.toList()
        );
    }

    private TransactionDto convertEntityToDto(Transaction transaction){
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }
}
