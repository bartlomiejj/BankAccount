package bank.currency.api.protocol;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CalculateRequest {
    @NotNull
    private BigDecimal sourceValue;
    @NotNull
    private String sourceCurrency;
    @NotNull
    private String targetCurrency;
}
