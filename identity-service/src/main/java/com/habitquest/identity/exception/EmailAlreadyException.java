package com.habitquest.identity.exception;

public class EmailAlreadyException extends RuntimeException {

    public EmailAlreadyException() {
        super("Пользователь с таким email уже существует");
    }
}
