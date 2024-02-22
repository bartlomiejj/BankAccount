package bank.currency.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
public class CurrenciesDto {

    private Collection<Currency> currencies;

    @Data
    public static class Currency {
        private String name;
        private String code;
        private BigDecimal mid;
        private Date lastUpdated;
        private String source;
    }
}
