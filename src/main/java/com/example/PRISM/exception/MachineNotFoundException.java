package com.example.PRISM.exception;

public class MachineNotFoundException extends RuntimeException{

    public MachineNotFoundException(String message){
        super(message);
    }

}
