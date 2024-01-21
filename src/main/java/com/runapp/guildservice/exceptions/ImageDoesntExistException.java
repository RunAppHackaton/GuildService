package com.runapp.guildservice.exceptions;

public class ImageDoesntExistException extends RuntimeException{
    public ImageDoesntExistException(){
        super("The image does not exist or the data was transferred incorrectly");
    }
}
