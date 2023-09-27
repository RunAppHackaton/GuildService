package com.runapp.guildservice.dto.request;

import com.runapp.guildservice.utill.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondToRequest {
    private int admin_id;
    private int team_id;
    private Long request_id;
    private RequestStatusEnum decision;
}
