package bank.account.infrastructure.db;

import bank.shared.CurrencyCode;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "accountNumber")
public class Account {

    private long accountNumber;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

}
