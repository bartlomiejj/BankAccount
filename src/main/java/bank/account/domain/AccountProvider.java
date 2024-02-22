package bank.account.domain;

import bank.account.domain.dto.AccountDto;
import bank.account.infrastructure.db.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Component
public class AccountProvider {

    private final CustomerRepository customerRepository;

    public AccountDto findByAccountNumber(String accountNumber) {
        return customerRepository.findByAccountsAccountNumber(Long.valueOf(accountNumber))
                .flatMap(entity -> entity.getAccounts().stream()
                        .filter(account -> accountNumber.equals(String.valueOf(account.getAccountNumber())))
                        .findFirst()
                        .map(AccountMapper.INSTANCE::mapToDto))
                .orElseThrow(EntityNotFoundException::new);
    }

}
