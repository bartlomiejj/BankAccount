package bank.account.domain;

import bank.account.api.protocol.AccountRequest;
import bank.account.domain.dto.AccountDto;
import bank.account.domain.dto.CustomerDto;
import bank.account.infrastructure.db.CustomerEntity;
import bank.account.infrastructure.db.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
class AccountServiceImpl implements AccountService {

    private final CustomerRepository customerRepository;

    private final AccountProvider accountProvider;

    @Override
    public CustomerDto create(AccountRequest accountRequest) {
        Optional<CustomerEntity> existingOwner = customerRepository.findByPesel(accountRequest.getPesel());

        return existingOwner.map(AccountMapper.INSTANCE::mapToDto)
                .map(dto -> {
                    dto.addAccount(createAccount(accountRequest));
                    customerRepository.save(AccountMapper.INSTANCE.update(existingOwner.get(), dto));
                    return dto;
                })
                .orElseGet(() -> createCustomerWithAccount(accountRequest, createAccount(accountRequest)));
    }

    private AccountDto createAccount(AccountRequest accountRequest) {
        AccountDto accountDto = AccountMapper.INSTANCE.mapToDto(accountRequest);
        accountDto.setAccountNumber(getAccountNumber());
        return accountDto;
    }

    @Override
    public AccountDto get(String accountNumber) {
        return accountProvider.findByAccountNumber(accountNumber);
    }

    @Override
    public CustomerDto getAllByCustomerPesel(String pesel) {
        return customerRepository.findByPesel(pesel)
                .map(AccountMapper.INSTANCE::mapToDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    private CustomerDto createCustomerWithAccount(AccountRequest request, AccountDto accountDto) {
        CustomerDto owner = new CustomerDto(request.getName(), request.getSurname(), request.getPesel());
        owner.addAccount(accountDto);

        CustomerEntity customerEntity = AccountMapper.INSTANCE.mapToEntity(owner);

        customerRepository.save(customerEntity);

        return owner;
    }

    private long getAccountNumber(){
        long min = 1000000000L;
        long max = 9999999999L;

        Random random = new Random();

        return min + (long) (random.nextDouble() * (max - min));
    }

}
