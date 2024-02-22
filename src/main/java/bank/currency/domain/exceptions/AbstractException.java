package bank.currency.domain.exceptions;

import java.util.UUID;

public abstract class AbstractException extends RuntimeException {

    private static final String uuid = UUID.randomUUID().toString();

    public AbstractException(String message) {
        super(prepareResponse(message));
    }

    public static String prepareResponse(String message) {
        return message.concat(" - ERROR UUID: ".concat(uuid));
    }
}
