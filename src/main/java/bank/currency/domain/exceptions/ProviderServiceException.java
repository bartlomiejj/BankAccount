package bank.currency.domain.exceptions;

public class ProviderServiceException extends AbstractException {

    private static final String PROVIDER_SERVICE_PROBLEM = "Problem with provider service occurred.";

    public ProviderServiceException() {
        super(PROVIDER_SERVICE_PROBLEM);
    }
}
