package com.runapp.guildservice.exceptions;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(int id){
        super("Team with id " + id + " not found");
    }
}
