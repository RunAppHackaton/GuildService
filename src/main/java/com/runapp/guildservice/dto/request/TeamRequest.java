package com.runapp.guildservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotEmpty(message = "Team name cannot be empty")
    @Length(min = 3, max = 15, message = "Team name must be between 3 and 15 characters")
    private String teamName;

    private String descriptionTeam;

    @Positive(message = "Story ID must be a positive integer")
    private int storyId;

    @Positive(message = "Maximum players must be a positive integer")
    private int maximumPlayers;

    private String adminId;
}
