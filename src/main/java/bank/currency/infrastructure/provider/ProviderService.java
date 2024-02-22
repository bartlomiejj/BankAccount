package bank.currency.infrastructure.provider;

import bank.currency.infrastructure.provider.protocol.AbstractProviderResponse;

public interface ProviderService {

    AbstractProviderResponse getCurrency(String target);

    AbstractProviderResponse getCurrencies();
}
