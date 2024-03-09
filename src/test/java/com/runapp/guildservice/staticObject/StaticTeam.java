package com.runapp.guildservice.staticObject;

import com.runapp.guildservice.dto.request.TeamRequest;
import com.runapp.guildservice.dto.request.TeamUpdateRequest;
import com.runapp.guildservice.dto.response.TeamResponse;
import com.runapp.guildservice.model.TeamModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class StaticTeam {
    static public TeamModel teamModel(){
        TeamModel teamModel = new TeamModel();
        teamModel.setAdminId("1");
        teamModel.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        teamModel.setDescriptionTeam("Description Team");
        teamModel.setId(1);
        teamModel.setMaximumPlayers(3);
        teamModel.setRanking(1L);
        teamModel.setStoryId(1);
        teamModel.setTeamImageUrl("https://example.org/example");
        teamModel.setTeamName("Team Name");
        teamModel.setUserTeamModelList(new ArrayList<>());
        return teamModel;
    }

    static public TeamModel teamModel2(){
        return new TeamModel(1, "Team1", "Description", null, "DEFAULT", 0L, 1, 5, "1", null);
    }

    static public TeamRequest teamRequest(){
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setAdminId("1");
        teamRequest.setDescriptionTeam("Description Team");
        teamRequest.setMaximumPlayers(3);
        teamRequest.setStoryId(1);
        teamRequest.setTeamName("Team Name");
        return teamRequest;
    }
    public static TeamRequest teamRequestBad(){
        return new TeamRequest("", "", -1, -1, "-1");
    }

    public static TeamUpdateRequest teamUpdateRequest(){
        TeamUpdateRequest teamUpdateRequest = new TeamUpdateRequest();
        teamUpdateRequest.setAdminId("0");
        teamUpdateRequest.setDescriptionTeam("Description Team");
        teamUpdateRequest.setMaximumPlayers(3);
        teamUpdateRequest.setRanking(1L);
        teamUpdateRequest.setStoryId(1);
        teamUpdateRequest.setTeamName("Team Name");
        return teamUpdateRequest;
    }

    public static TeamUpdateRequest teamUpdateRequestBad(){
        return new TeamUpdateRequest("", "", -1, -1, "-1", -1L);
    }


    static public TeamResponse teamResponse2(){
        return new TeamResponse(1, "Team1", "Description", null, "DEFAULT", 0L, 1, 5, "1", null);
    }
}
