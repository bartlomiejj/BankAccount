package bank.utils

import bank.currency.infrastructure.db.CurrencyEntity


class CurrenciesTestUtils {

    def static getExampleEntities() {
        return List.of(buildEntity(USD_CURRENCY_CODE), buildEntity(EUR_CURRENCY_CODE))
    }

    def static buildEntity(String currencyCode) {
        return new CurrencyEntity(
                name: "name",
                code: currencyCode,
                mid: 1.00,
                lastUpdated: new Date(1900-01-01),
                source: "notNBP"
        )
    }

    static String REFRESH = "/api/currency/refresh"
    static String NBP_TABLES_RESPONSE_JSON_PATH = "src/test/resources/NBPTablesResponse.json"
    static String NBP_RATES_RESPONSE_JSON_PATH = "src/test/resources/NBPRatesResponse.json"
    static String NBP_SOURCE = "NBP"
    static String USD_CURRENCY_CODE = "USD"
    static String EUR_CURRENCY_CODE = "EUR"
}
