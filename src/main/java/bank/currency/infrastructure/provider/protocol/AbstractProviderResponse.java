package bank.currency.infrastructure.provider.protocol;

import bank.shared.Currency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractProviderResponse {

    public abstract Collection<Currency> getCurrencies();
    public abstract Date getLastUpdated();
    public abstract String getSource();

    protected Date parseStringToDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(date);
        } catch (ParseException ignored) {
            throw new RuntimeException("Parsing date failed");
        }
    }
}
