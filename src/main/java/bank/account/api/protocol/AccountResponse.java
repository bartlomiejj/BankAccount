package bank.account.api.protocol;

import bank.shared.CurrencyCode;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AccountResponse implements Serializable {

    private static final long serialVersionUID = 4886803693138061041L;

    private String accountNumber;

    private CurrencyCode currencyCode;

    private BigDecimal balance;

}
