package bank.currency.domain;

import java.math.BigDecimal;

interface CurrencyService {

    BigDecimal getRate(String target);

    void refresh();

}
