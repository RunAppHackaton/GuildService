package com.runapp.guildservice.controller;

import com.runapp.guildservice.dto.request.RespondToRequest;
import com.runapp.guildservice.model.TeamMembershipRequestModel;
import com.runapp.guildservice.service.TeamMembershipRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-membership-requests")
@Tag(name = "Team Membership Request Management", description = "Operations related to team membership requests")
public class TeamMembershipRequestController {

    private final TeamMembershipRequestService teamMembershipRequestService;

    @Autowired
    public TeamMembershipRequestController(TeamMembershipRequestService teamMembershipRequestService) {
        this.teamMembershipRequestService = teamMembershipRequestService;
    }

    @PostMapping
    @Operation(summary = "Create a new team membership request", description = "Create a new team membership request with the provided data")
    @ApiResponse(responseCode = "201", description = "Team membership request created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Object> createTeamMembershipRequest(
            @Parameter(description = "User ID", required = true) @RequestParam("user_id") int userId,
            @Parameter(description = "Team ID", required = true) @RequestParam("team_id") int teamId) {
        return teamMembershipRequestService.createResponseToTeam(userId, teamId);
    }

    @GetMapping
    @Operation(summary = "Get all team membership requests", description = "Retrieve a list of all team membership requests")
    public ResponseEntity<List<TeamMembershipRequestModel>> getAllTeamMembershipRequests() {
        return teamMembershipRequestService.getAllRequestsToTeams();
    }

    @PutMapping("/respond")
    @Operation(summary = "Respond to a team membership request", description = "Respond to a team membership request by an administrator")
    @ApiResponse(responseCode = "200", description = "Response processed", content = @Content(schema = @Schema(implementation = TeamMembershipRequestModel.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Team membership request not found")
    public ResponseEntity<Object> respondToTeamMembershipRequest(@RequestBody RespondToRequest respondToRequest) {
        return teamMembershipRequestService.acceptRequestByAdministrator(respondToRequest);
    }
}
