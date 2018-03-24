package com.example.lib_native_net.error;


public class AppException extends Exception {

    public int statusCode;
    public String responseMessage;

    public enum ErrorType {CANCEL,TIMEOUT, SERVER, JSON, IO, FILE_NOT_FOUND, UPLOAD, MANUAL}
    public ErrorType type;

    public AppException(int status, String responseMessage) {
        super(responseMessage);
        this.type = ErrorType.SERVER;
        this.statusCode = status;
        this.responseMessage = responseMessage;
    }

    public AppException(ErrorType type,String detailMessage) {
        super(detailMessage);
        this.type = type;
    }
}
