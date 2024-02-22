package bank.account.api;

import bank.account.api.protocol.AccountRequest;
import bank.account.api.protocol.AccountResponse;
import bank.account.api.protocol.CustomerResponse;
import bank.account.domain.AccountFacade;
import bank.account.domain.AccountMapper;
import bank.account.domain.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountFacade accountFacade;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody AccountRequest accountRequest){
        CustomerDto customerDto = accountFacade.create(accountRequest);

        return ResponseEntity.ok(AccountMapper.INSTANCE.mapToResponse(customerDto));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> get(@PathVariable("accountNumber") String accountNumber){
        return ResponseEntity.ok(AccountMapper.INSTANCE.mapToResponse(accountFacade.get(accountNumber)));
    }

    @GetMapping("/customer/{pesel}")
    public ResponseEntity<CustomerResponse> getAllByCustomer(@PathVariable("pesel") String pesel){
        return ResponseEntity.ok(AccountMapper.INSTANCE.mapToResponse(accountFacade.getAllByCustomer(pesel)));
    }
}
