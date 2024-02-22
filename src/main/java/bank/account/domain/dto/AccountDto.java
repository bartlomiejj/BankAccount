package bank.account.domain.dto;

import bank.shared.CurrencyCode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private long accountNumber;

    private CurrencyCode currencyCode;

    private BigDecimal balance;

}
