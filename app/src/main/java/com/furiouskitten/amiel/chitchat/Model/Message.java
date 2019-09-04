package com.furiouskitten.amiel.chitchat.Model;

import androidx.annotation.NonNull;

public class Message {

    String message;
    String key;
    String name;

    public Message() {
    }

    public Message(String message, String name) {
        this.message = message;
        this.key = key;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        return "User{"+
                "message='"+ message + '\''+
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';

    }
}
