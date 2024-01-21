package com.runapp.guildservice.exceptions;

public class TeamBadRequestException extends RuntimeException{
    public TeamBadRequestException(int id){
        super("Team with id " + id + " not found");
    }
}
