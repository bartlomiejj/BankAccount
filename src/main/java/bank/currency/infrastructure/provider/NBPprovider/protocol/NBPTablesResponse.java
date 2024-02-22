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
public class NBPTablesResponse extends AbstractProviderResponse {

    private static final String SOURCE = "NBP_TABLES";

    private String table;

    private String no;

    private String effectiveDate;

    private Collection<Currency> rates = new HashSet<>();

    @Override
    public Collection<bank.shared.Currency> getCurrencies() {
        return rates.stream()
                .filter(Objects::nonNull)
                .map(nbpRate -> new bank.shared.Currency(nbpRate.currency, nbpRate.code, nbpRate.mid))
                .collect(Collectors.toSet());
    }

    @Override
    public Date getLastUpdated() {
        return parseStringToDate(effectiveDate);
    }

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Data
    public static class Currency {
        private String currency;
        private String code;
        private BigDecimal mid;
    }
}
