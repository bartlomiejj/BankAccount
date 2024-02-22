package bank.account.domain;

import bank.account.api.protocol.AccountRequest;
import bank.account.api.protocol.ExchangeRequest;
import bank.account.domain.dto.AccountDto;
import bank.account.domain.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountFacade {

    private final AccountService accountService;

    private final ExchangeService exchangeService;

    public CustomerDto create(AccountRequest accountRequest) {
        return accountService.create(accountRequest);
    }

    public AccountDto get(String accountNumber) {
        return accountService.get(accountNumber);
    }

    public CustomerDto getAllByCustomer(String pesel) {
        return accountService.getAllByCustomerPesel(pesel);
    };

    public CustomerDto exchange(ExchangeRequest exchangeRequest) {
        return exchangeService.exchange(exchangeRequest);
    }
}
