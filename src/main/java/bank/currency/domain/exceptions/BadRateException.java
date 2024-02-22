package bank.currency.domain.exceptions;

public class BadRateException extends AbstractException {

    public BadRateException(String message) {
        super(message);
    }
}
