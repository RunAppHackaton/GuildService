package com.runapp.guildservice.staticObject;

import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class StaticUserTeam {
    static public UserTeamModel userTeamModel(){
        TeamModel team = new TeamModel();
        team.setAdminId("1");
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId("1");
        return userTeamModel;
    }

    public static UserTeamRequest userTeamRequest(){
        UserTeamRequest userTeamRequest = new UserTeamRequest();
        userTeamRequest.setTeam_id(1);
        userTeamRequest.setUserId("1");
        return userTeamRequest;
    }

    public static UserTeamRequest userTeamRequestbad(){
        return new UserTeamRequest("0", 0);
    }

    public static UserTeamResponse userTeamResponse(){
        return new UserTeamResponse(1, "1", 1);
    }
}
