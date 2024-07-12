package ru.cft.template.core.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {
    public static final HttpStatus OBJECT_NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus INVALID_REQUEST = HttpStatus.BAD_REQUEST;
    public static final HttpStatus ITERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;
}
