package com.runapp.guildservice.controller;

import com.runapp.guildservice.dto.request.DeleteStorageRequest;
import com.runapp.guildservice.dto.request.TeamDeleteRequest;
import com.runapp.guildservice.dto.request.TeamRequest;
import com.runapp.guildservice.dto.request.TeamUpdateRequest;
import com.runapp.guildservice.dto.response.*;
import com.runapp.guildservice.feignClient.ProfileServiceClient;
import com.runapp.guildservice.feignClient.StorageServiceClient;
import com.runapp.guildservice.feignClient.StoryManagementServiceClient;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.dto.dtoMapper.TeamDtoMapper;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
@Tag(name = "Team Management", description = "Operations related to teams")
public class TeamController {
    //comment
    private final TeamService teamService;
    private final TeamDtoMapper teamDtoMapper;

    private final ProfileServiceClient profileServiceClient;

    private final StoryManagementServiceClient storyManagementServiceClient;

    @Value("${storage-directory}")
    private String storageDirectory;
    private final StorageServiceClient storageServiceClient;

    @Autowired
    public TeamController(TeamService teamService, TeamDtoMapper teamDtoMapper, ProfileServiceClient profileServiceClient, StoryManagementServiceClient storyManagementServiceClient, StorageServiceClient storageServiceClient) {
        this.teamService = teamService;
        this.teamDtoMapper = teamDtoMapper;
        this.profileServiceClient = profileServiceClient;
        this.storyManagementServiceClient = storyManagementServiceClient;
        this.storageServiceClient = storageServiceClient;
    }

    @PostMapping
    @Operation(summary = "Create a new team", description = "Create a new team with the provided data")
    @ApiResponse(responseCode = "201", description = "Team created", content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Object> createTeam(@Parameter(description = "Team data", required = true) @Valid @RequestBody TeamRequest teamRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {

            StoryResponse storyResponse = storyManagementServiceClient.getStoryById(teamRequest.getStoryId()).getBody();
            UserResponse userResponse = profileServiceClient.getUserById(teamRequest.getAdminId()).getBody();
            TeamModel teamModel = teamDtoMapper.toModel(teamRequest);
            TeamModel createdTeam = teamService.createTeam(teamModel);
            TeamResponse teamResponse = teamDtoMapper.toResponse(createdTeam);
            return new ResponseEntity<>(teamResponse, HttpStatus.CREATED);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(LocalDateTime.now(), "Story or Admin with ID " + teamRequest.getStoryId() + " not found"));
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(LocalDateTime.now(), "No such history or user exists"));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a team by ID", description = "Retrieve a team by its ID")
    @ApiResponse(responseCode = "200", description = "Team found", content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponse(responseCode = "404", description = "Team not found")
    public ResponseEntity<TeamResponse> getTeamById(@Parameter(description = "Team ID", required = true) @PathVariable int id) {
        return teamService.getTeamById(id)
                .map(teamDtoMapper::toResponse)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Get all teams", description = "Retrieve a list of all teams")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<TeamResponse> teams = teamService.getAllTeams().stream()
                .map(teamDtoMapper::toResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a team", description = "Update an existing team with the provided data")
    @ApiResponse(responseCode = "200", description = "Team updated", content = @Content(schema = @Schema(implementation = TeamResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Team not found")
    public ResponseEntity<Object> updateTeam(@Parameter(description = "Team ID", required = true) @PathVariable int id, @Valid @RequestBody TeamUpdateRequest teamUpdateRequest, BindingResult bindingResult) {
        Optional<TeamModel> optionalTeamModel = teamService.getTeamById(id);
        if (optionalTeamModel.isEmpty())
            return ResponseEntity.badRequest().body(
                    new ExceptionResponse(LocalDateTime.now(), "Team with id " + id + " not found"));
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            StoryResponse storyResponse = storyManagementServiceClient.getStoryById(teamUpdateRequest.getStoryId()).getBody();
            UserResponse userResponse = profileServiceClient.getUserById(teamUpdateRequest.getAdminId()).getBody();
            TeamModel teamModel = optionalTeamModel.orElse(null);
            teamModel.setTeamName(teamUpdateRequest.getTeamName());
            teamModel.setDescriptionTeam(teamUpdateRequest.getDescriptionTeam());
            teamModel.setStoryId(teamUpdateRequest.getStoryId());
            teamModel.setMaximumPlayers(teamUpdateRequest.getMaximumPlayers());
            teamModel.setAdminId(teamUpdateRequest.getAdminId());
            teamModel.setRanking(teamUpdateRequest.getRanking());
            TeamModel updatedTeam = teamService.updateTeam(id, teamModel);
            TeamResponse teamResponse = teamDtoMapper.toResponse(updatedTeam);
            return new ResponseEntity<>(teamResponse, HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(LocalDateTime.now(), "Story or Admin ID " + teamUpdateRequest.getStoryId() + " not found"));
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(LocalDateTime.now(), "No such history or user exists"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a team", description = "Delete a team by its ID")
    @ApiResponse(responseCode = "204", description = "Team deleted")
    @ApiResponse(responseCode = "404", description = "Team not found")
    public ResponseEntity<Object> deleteTeam(@Parameter(description = "Team ID", required = true)
                                             @PathVariable int id) {
        Optional<TeamModel> teamModel = teamService.getTeamById(id);
        if (teamModel.isEmpty())
            return ResponseEntity.badRequest().body(
                    new ExceptionResponse(LocalDateTime.now(), "Team with id " + id + " not found"));
        teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/upload-image")
    @Operation(summary = "Upload an image for a team", description = "Upload an image file for a specific team by providing the file and team ID.")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = @Content(schema = @Schema(implementation = TeamModel.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Team not found")
    public ResponseEntity<Object> uploadImage(
            @Parameter(description = "Team ID", required = true) @RequestParam("team_id") int team_id,
            @Parameter(description = "Image file to upload", required = true) @RequestParam("file") MultipartFile file) {
        Optional<TeamModel> optionalTeamModel = teamService.getTeamById(team_id);
        if (optionalTeamModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team with id " + team_id + " not found");
        } else {
            TeamModel teamModel = optionalTeamModel.get();
            teamModel.setTeamImageUrl(storageServiceClient.uploadFile(file, storageDirectory).getFile_uri());
            teamService.updateTeam(team_id, teamModel);
            return ResponseEntity.ok().body(teamModel);
        }
    }

    @DeleteMapping("/delete-image")
    @Operation(summary = "Delete an image associated with a team", description = "Delete the image associated with a team by providing the image URI and team details.")
    @ApiResponse(responseCode = "200", description = "Image deleted successfully")
    @ApiResponse(responseCode = "404", description = "Team not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Object> deleteImage(
            @Parameter(description = "Team ID", required = true) @RequestBody TeamDeleteRequest teamDeleteRequest) {
        Optional<TeamModel> optionalTeamModel = teamService.getTeamById(teamDeleteRequest.getTeam_id());
        if (optionalTeamModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team with id " + teamDeleteRequest.getTeam_id() + " not found");
        }
        TeamModel teamModel = optionalTeamModel.get();
        teamModel.setTeamImageUrl("DEFAULT");
        teamService.updateTeam(teamDeleteRequest.getTeam_id(), teamModel);
        try {
            storageServiceClient.deleteFile(new DeleteStorageRequest(teamDeleteRequest.getFile_uri(), storageDirectory));
            return ResponseEntity.ok().build();
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse("the image does not exist or the data was transferred incorrectly"));
        }
    }
}

