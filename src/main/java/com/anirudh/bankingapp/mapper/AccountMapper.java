package com.anirudh.bankingapp.mapper;

import com.anirudh.bankingapp.dto.AccountDto;
import com.anirudh.bankingapp.model.Account;

public class AccountMapper {

    public static AccountDto mapToAccountDto(Account account) {
        return new AccountDto(account.getId(), account.getHolderName(), account.getBalance());
    }

    public static Account mapToAccount(AccountDto accountDto) {
        return new Account(accountDto.id(), accountDto.holderName(), accountDto.balance());
    }
}
