package bank.account.domain

import bank.account.api.protocol.AccountRequest
import bank.account.domain.dto.AccountDto
import bank.account.domain.dto.CustomerDto
import bank.account.infrastructure.db.Account
import bank.account.infrastructure.db.CustomerEntity
import bank.account.infrastructure.db.CustomerRepository
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import spock.lang.Specification

class AccountServiceImplTest extends Specification {

    @Subject
    AccountServiceImpl accountService

    @Collaborator
    CustomerRepository customerRepository = Mock(CustomerRepository.class)

    @Collaborator
    AccountProvider accountProvider = Mock(AccountProvider.class)

    def "create method should return a CustomerDto with a new account when the customer already exists"() {
        given:
            def accountRequest = new AccountRequest(name: "John", surname: "Doe", pesel: "1234567890")
            def existingCustomerEntity = new CustomerEntity(name: "John", surname: "Doe", pesel: "1234567890")
            existingCustomerEntity.accounts = [new Account(accountNumber: 123456789L)]

            customerRepository.findByPesel("1234567890") >> Optional.of(existingCustomerEntity)

        when:
            def result = accountService.create(accountRequest)

        then:
            1 * customerRepository.save(_) >> existingCustomerEntity
            result.accounts.size() == 2
    }

    def "create method should return a CustomerDto with a new customer and account when the customer does not exist"() {
        given:
            def accountRequest = new AccountRequest(name: "John", surname: "Doe", pesel: "1234567890")

            customerRepository.findByPesel("1234567890") >> Optional.empty()

        when:
            def result = accountService.create(accountRequest)

            then:
            1 * customerRepository.save(_) >> _
            result.accounts.size() == 1
    }

    def "get method should return an AccountDto for a given account number"() {
        given:
            String accountNumber = "123456789"
            AccountDto expectedAccountDto = new AccountDto(accountNumber: Long.valueOf(accountNumber))

            accountProvider.findByAccountNumber(accountNumber) >> expectedAccountDto

        when:
            AccountDto result = accountService.get(accountNumber)

        then:
            result == expectedAccountDto
    }

    def "getAllByCustomerPesel method should return a CustomerDto for a given customer pesel"() {
        given:
            String pesel = "1234567890"
            CustomerEntity existingCustomerEntity = new CustomerEntity(name: "John", surname: "Doe", pesel: pesel)
            existingCustomerEntity.accounts = [new Account(accountNumber: 123456789L)]

            customerRepository.findByPesel(pesel) >> Optional.of(existingCustomerEntity)

        when:
            CustomerDto result = accountService.getAllByCustomerPesel(pesel)

        then:
            result.accounts.size() == 1
    }
}