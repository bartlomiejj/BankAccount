package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String pesel;

    @NotNull(message = "currencyCode cannot be null")
    private CurrencyCode currencyCode;

    @NotNull
    private BigDecimal initialBalance;
}
