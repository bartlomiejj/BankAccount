package bank.currency.domain.exceptions;

public class NoCurrencyException extends AbstractException {

    public NoCurrencyException(String message) {
        super(message);
    }
}
