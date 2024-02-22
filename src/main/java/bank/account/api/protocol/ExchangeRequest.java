package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @NotNull
    @Min(9)
    @Max(9)
    @Pattern(regexp = "\\d")
    private String pesel;

    @NotNull
    @Min(10)
    @Max(10)
    @Pattern(regexp = "\\d")
    private String sourceAccount;

    @NotNull
    private CurrencyCode sourceCurrency;

    @NotNull
    @Min(10)
    @Max(10)
    @Pattern(regexp = "\\d")
    private String targetAccount;

    @NotNull
    private CurrencyCode targetCurrency;

    @NotNull
    private BigDecimal targetValue;

    public boolean toPln(){
        return CurrencyCode.PLN == targetCurrency;
    }

    public CurrencyCode getCurrencyToRate(){
        return toPln() ? sourceCurrency : targetCurrency;
    }
}
