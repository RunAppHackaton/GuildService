package com.runapp.guildservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeamResponse {
    private int id;

    private int userId;

    private int teamId;
}
