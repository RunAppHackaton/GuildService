package com.runapp.guildservice.exceptions;

public class UserTeamNotFoundException extends RuntimeException{
    public UserTeamNotFoundException(int id){
        super("User-Team with id " + id + " not found");
    }
}
