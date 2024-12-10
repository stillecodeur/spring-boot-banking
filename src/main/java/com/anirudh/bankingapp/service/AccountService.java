package com.anirudh.bankingapp.service;

import com.anirudh.bankingapp.dto.AccountDto;
import com.anirudh.bankingapp.dto.TransferFundDto;

import java.util.List;

public interface AccountService {

     AccountDto createAccount(AccountDto accountDto);

     AccountDto getAccount(Long id);

     List<AccountDto> getAccounts();

     AccountDto depositAmount(Long id,double amount);

     AccountDto withdrawAmount(Long id,double amount);

     void deleteAccount(Long id);

     void transferFunds(TransferFundDto transferFundDto);
}
