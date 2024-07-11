package com.flaze.exception;

public class ArticleAlreadyExistException extends Exception{
    public ArticleAlreadyExistException(String message) {
        super(message);
    }
}
