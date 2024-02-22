package bank.account.api;

import bank.account.api.protocol.CustomerResponse;
import bank.account.api.protocol.ExchangeRequest;
import bank.account.domain.AccountFacade;
import bank.account.domain.AccountMapper;
import bank.account.domain.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final AccountFacade accountFacade;

    @PutMapping
    public ResponseEntity<CustomerResponse> exchange(@Valid @RequestBody ExchangeRequest exchangeRequest){
        CustomerDto customerDto = accountFacade.exchange(exchangeRequest);

        return ResponseEntity.ok(AccountMapper.INSTANCE.mapToResponse(customerDto));
    }

}
