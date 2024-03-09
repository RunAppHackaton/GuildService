package com.runapp.guildservice.dtoMapper;

import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;
import org.springframework.stereotype.Component;

@Component
public class UserTeamDtoMapper {

    public UserTeamModel toModel(UserTeamRequest request, TeamModel teamModel) {
        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setUserId(request.getUserId());
        userTeamModel.setTeam(teamModel);
        return userTeamModel;
    }

    public UserTeamResponse toResponse(UserTeamModel model) {
        UserTeamResponse userTeamResponse = new UserTeamResponse();
        userTeamResponse.setId(model.getId());
        userTeamResponse.setUserId(model.getUserId());
        userTeamResponse.setTeamId(model.getTeam().getId());
        return userTeamResponse;
    }
}
