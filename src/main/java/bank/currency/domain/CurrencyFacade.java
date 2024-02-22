package bank.currency.domain;

import bank.shared.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyFacade {

    private final CurrencyService currencyService;

    public BigDecimal getRate(CurrencyCode target) {
        return currencyService.getRate(target.name());
    }

    @EventListener(ContextRefreshedEvent.class)
    public void refresh() {
        currencyService.refresh();
    }

}
