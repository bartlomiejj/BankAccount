package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AccountRequest implements Serializable {

    private static final long serialVersionUID = -3137249684522196459L;

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
