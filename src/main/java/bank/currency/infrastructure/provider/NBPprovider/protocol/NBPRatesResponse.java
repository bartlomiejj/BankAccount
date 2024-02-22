package bank.currency.infrastructure.provider.NBPprovider.protocol;

import bank.currency.infrastructure.provider.protocol.AbstractProviderResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class NBPRatesResponse extends AbstractProviderResponse {

    private static final String SOURCE = "NBP_RATES";

    private String table;
    private String currency;
    private String code;
    private Collection<Currency> rates = new HashSet<>();

    @Override
    public Collection<bank.shared.Currency> getCurrencies() {
        return rates.stream()
                .filter(Objects::nonNull)
                .map(nbpRate -> new bank.shared.Currency(currency, code, nbpRate.mid))
                .collect(Collectors.toSet());
    }

    @Override
    public Date getLastUpdated() {
        return rates.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(currency -> parseStringToDate(currency.getEffectiveDate()))
                .orElse(null);
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Data
    public static class Currency {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;
    }
}
