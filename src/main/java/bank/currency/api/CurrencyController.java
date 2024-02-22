package bank.currency.api;

import bank.currency.domain.CurrencyFacade;
import bank.shared.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyFacade currencyFacade;

    @GetMapping("/{code}")
    public ResponseEntity<BigDecimal> calculateCurrency(@PathVariable("code") String code) {
        return ResponseEntity.ok(currencyFacade.getRate(CurrencyCode.valueOf(code.toUpperCase())));
    }

    @PutMapping("/refresh")
    public void refreshCurrency(){
        currencyFacade.refresh();
    }
}
