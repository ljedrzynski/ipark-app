package pl.ljedrzynski.iparkapp.common.exception;

import java.text.MessageFormat;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message, Object... vars) {
        super(MessageFormat.format(message, vars));
    }
}
