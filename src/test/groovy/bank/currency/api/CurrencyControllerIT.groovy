package bank.currency.api

import bank.IntegrationTest
import bank.currency.infrastructure.db.CurrencyEntity
import bank.currency.infrastructure.db.CurrencyRepository
import bank.currency.infrastructure.provider.NBPprovider.protocol.NBPRatesResponse
import bank.currency.infrastructure.provider.NBPprovider.protocol.NBPTablesResponse
import bank.shared.Currency
import bank.utils.CurrenciesTestUtils
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CurrencyControllerIT extends IntegrationTest {

    @Autowired
    CurrencyRepository currencyRepository

    def cleanup() {
        currencyRepository.deleteAll()
    }

    def "should return single currency rate"() {
        given:
            def responseJson = new File(CurrenciesTestUtils.NBP_RATES_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPRatesResponse.class)
            def exampleResponseCurrency = nbpResponse.currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE}

        when:
             restTemplate.getForObject(_, _) >> nbpResponse

             mockMvc.perform(get(CurrenciesTestUtils.RATE + CurrenciesTestUtils.USD_CURRENCY_CODE)).andExpect(status().isOk())

        then:
            def entities = currencyRepository.findAll()
            entities.size() == nbpResponse.currencies.size()
            def entity = entities.find({
                CurrencyEntity it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE })
            entity.name == exampleResponseCurrency.name
            entity.code == exampleResponseCurrency.code
            entity.mid == exampleResponseCurrency.mid
            entity.lastUpdated == nbpResponse.lastUpdated
            entity.source == CurrenciesTestUtils.NBP_RATES_SOURCE
    }

    def "should save currencies"() {
        given:
            def responseJson = new File(CurrenciesTestUtils.NBP_TABLES_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPTablesResponse[].class)
            def exampleResponseCurrency = nbpResponse[0].currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE}

        when:
             restTemplate.getForObject(_, _) >> nbpResponse

             mockMvc.perform(put(CurrenciesTestUtils.REFRESH)).andExpect(status().isOk())

        then:
            def entities = currencyRepository.findAll()
            entities.size() == nbpResponse[0].currencies.size()
            def entity = entities.find({
                CurrencyEntity it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE })
            entity.name == exampleResponseCurrency.name
            entity.code == exampleResponseCurrency.code
            entity.mid == exampleResponseCurrency.mid
            entity.lastUpdated == nbpResponse[0].lastUpdated
            entity.source == CurrenciesTestUtils.NBP_TABLES_SOURCE
    }

    def "should refresh currencies"() {
        given:
            def savedCurrencyUsd = (CurrencyEntity) currencyRepository.save(CurrenciesTestUtils.buildEntity(CurrenciesTestUtils.USD_CURRENCY_CODE))
                    .find({ CurrencyEntity it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE })
            def savedCurrencyEur = (CurrencyEntity) currencyRepository.save(CurrenciesTestUtils.buildEntity(CurrenciesTestUtils.EUR_CURRENCY_CODE))
                    .find({ CurrencyEntity it -> it.code == CurrenciesTestUtils.EUR_CURRENCY_CODE })
            def responseJson = new File(CurrenciesTestUtils.NBP_TABLES_RESPONSE_JSON_PATH)
            def nbpResponse = objectMapper.readValue(responseJson, NBPTablesResponse[].class)
            def exampleResponseCurrencyUsd = nbpResponse[0].currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE}
            def exampleResponseCurrencyEur = nbpResponse[0].currencies.find {
                Currency it -> it.code == CurrenciesTestUtils.EUR_CURRENCY_CODE}

        when:
            restTemplate.getForObject(_, _) >> nbpResponse

            mockMvc.perform(put(CurrenciesTestUtils.REFRESH)).andExpect(status().isOk())

        then:
            def documents = currencyRepository.findAll()
            documents.size() == nbpResponse[0].currencies.size()
            def usdCurrency = documents.find
                    { CurrencyEntity it -> it.code == CurrenciesTestUtils.USD_CURRENCY_CODE }
            def eurCurrency = documents.find
                    { CurrencyEntity it -> it.code == CurrenciesTestUtils.EUR_CURRENCY_CODE }

            usdCurrency.name == exampleResponseCurrencyUsd.name
            usdCurrency.code == savedCurrencyUsd.code && exampleResponseCurrencyUsd.code
            usdCurrency.mid != savedCurrencyUsd.mid
            usdCurrency.mid == exampleResponseCurrencyUsd.mid
            usdCurrency.lastUpdated != savedCurrencyUsd.lastUpdated
            usdCurrency.lastUpdated == nbpResponse[0].lastUpdated
            usdCurrency.source == CurrenciesTestUtils.NBP_TABLES_SOURCE

            eurCurrency.name == exampleResponseCurrencyEur.name
            eurCurrency.code == savedCurrencyEur.code && exampleResponseCurrencyEur.code
            eurCurrency.mid != savedCurrencyEur.mid
            eurCurrency.mid == exampleResponseCurrencyEur.mid
            eurCurrency.lastUpdated != savedCurrencyEur.lastUpdated
            eurCurrency.lastUpdated == nbpResponse[0].lastUpdated
            eurCurrency.source == CurrenciesTestUtils.NBP_TABLES_SOURCE
    }

}
