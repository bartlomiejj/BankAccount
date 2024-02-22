package bank.account.domain

import bank.account.api.protocol.ExchangeRequest
import bank.account.infrastructure.db.Account
import bank.account.infrastructure.db.CustomerEntity
import bank.account.infrastructure.db.CustomerRepository
import bank.currency.domain.CurrencyFacade
import bank.shared.CurrencyCode
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification
import spock.lang.Unroll

class ExchangeServiceImplTest extends Specification {

    @Subject
    ExchangeServiceImpl exchangeService

    @Collaborator
    CustomerRepository customerRepository = Mock(CustomerRepository.class)
    @Collaborator
    CurrencyFacade currencyFacade = Mock(CurrencyFacade.class)

    @Unroll("targetCurrency: #targetCurrency")
    def "exchange method should perform currency exchange when funds are sufficient"() {
        given:
            def exchangeRequest = new ExchangeRequest(
                    pesel: "1",
                    sourceCurrency: sourceCurrency,
                    sourceAccount: "1",
                    targetAccount: "2",
                    targetCurrency: targetCurrency,
                    targetValue: 1.0
            )
            def customerEntity = new CustomerEntity()
            customerEntity.accounts = [
                    new Account(accountNumber: 1L, currencyCode: sourceCurrency, balance: 10.0),
                    new Account(accountNumber: 2L, currencyCode: targetCurrency, balance: 10.0)
            ]
            customerRepository.findByPesel(exchangeRequest.pesel) >> Optional.of(customerEntity)
            currencyFacade.getRate(exchangeRequest.currencyToRate) >> 1.0

        when:
            def result = exchangeService.exchange(exchangeRequest)

        then:
            1 * customerRepository.save(_) >> customerEntity
            result.accounts.find { it.currencyCode == targetCurrency }.balance == 11
            result.accounts.find { it.currencyCode == sourceCurrency }.balance == 9

        where:
            sourceCurrency | targetCurrency
            CurrencyCode.PLN | CurrencyCode.USD
            CurrencyCode.USD | CurrencyCode.PLN
    }

    def "exchange method should throw an exception when funds are insufficient"() {
        given:
            def exchangeRequest = new ExchangeRequest(
                    pesel: "1",
                    sourceCurrency: "USD",
                    sourceAccount: "1",
                    targetAccount: "2",
                    targetCurrency: "PLN",
                    targetValue: 100.0
            )
            def customerEntity = new CustomerEntity()
            customerEntity.accounts = [
                    new Account(accountNumber: 1L, balance: 1.0),
                    new Account(accountNumber: 2L, balance: 1.0)
            ]
            customerRepository.findByPesel(exchangeRequest.pesel) >> Optional.of(customerEntity)
            currencyFacade.getRate(CurrencyCode.USD) >> 1

        when:
            exchangeService.exchange(exchangeRequest)

        then:
            def exception = thrown(ResponseStatusException)
            exception.status == HttpStatus.BAD_REQUEST
            exception.message.contains("Insufficient funds")
    }

    def "exchange method should throw an exception when customer not found"() {
        given:
            def exchangeRequest = new ExchangeRequest(
                    pesel: "1"
            )

            customerRepository.findByPesel(exchangeRequest.pesel) >> Optional.empty()
            currencyFacade.getRate(CurrencyCode.USD) >> 1

        when:
            exchangeService.exchange(exchangeRequest)

        then:
            def exception = thrown(ResponseStatusException)
            exception.status == HttpStatus.NOT_FOUND
            exception.message.contains("No customer with provided pesel")
    }

    def "exchange method should throw an exception when request account not found"() {
        given:
        def exchangeRequest = new ExchangeRequest(
                pesel: "1",
                sourceAccount: "1",
                targetAccount: "2",
        )
        def customerEntity = new CustomerEntity()
        customerEntity.accounts = [
                new Account(accountNumber: 1L, balance: 1.0)]
        customerRepository.findByPesel(exchangeRequest.pesel) >> Optional.of(customerEntity)
        currencyFacade.getRate(CurrencyCode.USD) >> 1

        when:
        exchangeService.exchange(exchangeRequest)

        then:
        def exception = thrown(ResponseStatusException)
        exception.status == HttpStatus.NOT_FOUND
        exception.message.contains("Account not found: " + exchangeRequest.targetAccount)
    }
}
