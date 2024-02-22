package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    private String accountNumber;

    private CurrencyCode currencyCode;

    private BigDecimal balance;

}
