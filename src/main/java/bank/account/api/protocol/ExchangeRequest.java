package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @NotBlank
    private String pesel;

    @NotBlank
    private String sourceAccount;

    @NotNull(message = "currencyCode cannot be null")
    private CurrencyCode sourceCurrency;

    @NotBlank
    private String targetAccount;

    @NotNull(message = "currencyCode cannot be null")
    private CurrencyCode targetCurrency;

    @NotNull
    @Min(1)
    private BigDecimal targetValue;

    public boolean toPln(){
        return CurrencyCode.PLN == targetCurrency;
    }

    public CurrencyCode getCurrencyToRate(){
        return toPln() ? sourceCurrency : targetCurrency;
    }
}
