package com.progrohan.weather.exception;

public class DataExistException extends RuntimeException{
    public DataExistException(String msg){
        super(msg);
    }
}
