package bank.account.domain;

import bank.account.api.protocol.ExchangeRequest;
import bank.account.domain.dto.CustomerDto;
import bank.account.infrastructure.db.Account;
import bank.account.infrastructure.db.CustomerEntity;
import bank.account.infrastructure.db.CustomerRepository;
import bank.currency.domain.CurrencyFacade;
import bank.shared.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
class ExchangeServiceImpl implements ExchangeService {

    private final CustomerRepository customerRepository;

    private final CurrencyFacade currencyFacade;

    public CustomerDto exchange(ExchangeRequest exchangeRequest) {
        CustomerEntity customerEntity = findCustomerByPesel(exchangeRequest.getPesel());
        Account sourceAccount = findAccountByNumber(customerEntity, exchangeRequest.getSourceAccount());
        Account targetAccount = findAccountByNumber(customerEntity, exchangeRequest.getTargetAccount());

        BigDecimal cost = calculateCost(exchangeRequest.getCurrencyToRate(), exchangeRequest.getTargetValue(),
                exchangeRequest.toPln());

        validateFundsAvailability(sourceAccount.getBalance(), cost);

        performExchange(sourceAccount, targetAccount, cost, exchangeRequest.getTargetValue());

        return persist(customerEntity);
    }

    private CustomerEntity findCustomerByPesel(String pesel) {
        return customerRepository.findByPesel(pesel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer with provided pesel"));
    }

    private Account findAccountByNumber(CustomerEntity customerEntity, String accountNumber) {
        return customerEntity.getAccounts().stream()
                .filter(account -> accountNumber.equals(String.valueOf(account.getAccountNumber())))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found: " + accountNumber));
    }

    private void validateFundsAvailability(BigDecimal sourceBalance, BigDecimal cost) {
        if (sourceBalance.compareTo(cost) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }
    }

    private void performExchange(Account sourceAccount, Account targetAccount, BigDecimal cost, BigDecimal profit) {
        updateAccountBalance(sourceAccount, sourceAccount.getBalance().subtract(cost));
        updateAccountBalance(targetAccount, targetAccount.getBalance().add(profit));
    }

    private void updateAccountBalance(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
    }

    private BigDecimal calculateCost(CurrencyCode currencyCode, BigDecimal targetValue, boolean toPln) {
        BigDecimal rate = currencyFacade.getRate(currencyCode);
        return toPln ? targetValue.divide(rate, 2, RoundingMode.DOWN) : targetValue.multiply(rate);
    }

    private CustomerDto persist(CustomerEntity customerEntity) {
        return AccountMapper.INSTANCE.mapToDto(customerRepository.save(customerEntity));
    }

}
