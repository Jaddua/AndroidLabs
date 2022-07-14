package com.cst2335.jone0648;
public class Messages {
    String message;
    public  Boolean sr;
    long id;

    Messages(String message, Boolean sr, long id){
        this.message = message;
        this.sr = sr;
        this.id = id;

    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return this.id;
    }
}