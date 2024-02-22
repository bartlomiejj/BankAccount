package bank.currency.infrastructure.provider.NBPprovider;

import bank.currency.domain.exceptions.ProviderServiceException;
import bank.currency.infrastructure.provider.NBPprovider.protocol.NBPRatesResponse;
import bank.currency.infrastructure.provider.NBPprovider.protocol.NBPTablesResponse;
import bank.currency.infrastructure.provider.ProviderConfig;
import bank.currency.infrastructure.provider.ProviderService;
import bank.currency.infrastructure.provider.protocol.AbstractProviderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NBPService implements ProviderService {

    private final RestTemplate restTemplate;

    private final ProviderConfig providerConfig;

    @Override
    public AbstractProviderResponse getCurrency(String target) {
        try {
            log.info("Getting rate for {} from NBP service started.", target);
            URI uri = new URI(providerConfig.getRatesUri() + target + "/today");

            NBPRatesResponse response = Optional.ofNullable(restTemplate.getForObject(uri.toString(), NBPRatesResponse.class))
                    .orElseGet(NBPRatesResponse::new);
            log.info("Getting rate for {} from NBP service finished.", target);
            return response;
        } catch (RestClientException e) {
            log.error("Error while getting rate for {}.", target, e);
            throw new ProviderServiceException();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    } 
    
    @Override
    public AbstractProviderResponse getCurrencies() {
        try {
            log.info("Getting currencies from NBP service started.");
            NBPTablesResponse[] response = Optional.ofNullable(restTemplate.getForObject(providerConfig.getTablesUri(), NBPTablesResponse[].class))
                    .orElseGet(() -> new NBPTablesResponse[0]);
            log.info("Getting currencies from NBP service finished.");
            return Arrays.stream(response)
                    .findFirst()
                    .orElseThrow(ProviderServiceException::new);
        } catch (RestClientException e) {
            log.error("Error while getting currencies.", e);
            throw new ProviderServiceException();
        }
    }
}
