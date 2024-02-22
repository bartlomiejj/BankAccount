package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExchangeRequest implements Serializable {

    private static final long serialVersionUID = 5524893018041849092L;

    @NotBlank
    private String pesel;

    @NotBlank
    private String sourceAccount;

    @NotNull(message = "sourceCurrency cannot be null")
    private CurrencyCode sourceCurrency;

    @NotBlank
    private String targetAccount;

    @NotNull(message = "targetCurrency cannot be null")
    private CurrencyCode targetCurrency;

    @NotNull
    @Min(value = 1, message = "minimum exchange value is 1")
    private BigDecimal targetValue;

    public boolean toPln(){
        return CurrencyCode.PLN == targetCurrency;
    }

    public CurrencyCode getCurrencyToRate(){
        return toPln() ? sourceCurrency : targetCurrency;
    }
}
