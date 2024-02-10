package com.example.TM30_LanisterPayFCS.exceptions;

public class ConfigurationNotFoundException extends RuntimeException{
    public ConfigurationNotFoundException(String message){
        super(message);
    }
}
