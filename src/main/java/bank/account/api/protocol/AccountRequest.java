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
    @Min(9)
    @Max(9)
    @Pattern(regexp = "\\d")
    private String pesel;

    @NotBlank
    private CurrencyCode currencyCode;

    @NotNull
    private BigDecimal initialBalance;
}
