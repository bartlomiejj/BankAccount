package bank.account.domain;

import bank.account.api.protocol.AccountRequest;
import bank.account.domain.dto.AccountDto;
import bank.account.domain.dto.CustomerDto;

interface AccountService {

    CustomerDto create(AccountRequest accountRequest);

    AccountDto get(String accountNumber);

    CustomerDto getAllByCustomerPesel(String pesel);
}
