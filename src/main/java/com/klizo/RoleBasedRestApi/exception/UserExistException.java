package com.klizo.RoleBasedRestApi.exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String message){super(message);}
}
