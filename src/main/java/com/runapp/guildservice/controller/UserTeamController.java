package com.runapp.guildservice.controller;

import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserResponse;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.exceptions.TeamBadRequestException;
import com.runapp.guildservice.exceptions.UserTeamNotFoundException;
import com.runapp.guildservice.feignClient.ProfileServiceClient;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.service.UserTeamService;
import com.runapp.guildservice.dto.dtoMapper.UserTeamDtoMapper;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userteams")
public class UserTeamController {

    private final UserTeamService userTeamService;
    private final UserTeamDtoMapper userTeamDtoMapper;
    private final TeamService teamService;

    private final ProfileServiceClient profileServiceClient;

    @Autowired
    public UserTeamController(UserTeamService userTeamService, UserTeamDtoMapper userTeamDtoMapper, TeamService teamService, ProfileServiceClient profileServiceClient) {
        this.userTeamService = userTeamService;
        this.userTeamDtoMapper = userTeamDtoMapper;
        this.teamService = teamService;
        this.profileServiceClient = profileServiceClient;
    }

    @PostMapping
    public ResponseEntity<Object> createUserTeam(@Valid @RequestBody UserTeamRequest userTeamRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handling validation errors
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        TeamModel teamModel = teamService.getTeamById(userTeamRequest.getTeam_id()).orElseThrow(()->new TeamBadRequestException(userTeamRequest.getTeam_id()));
        UserTeamModel userTeamModel = userTeamDtoMapper.toModel(userTeamRequest, teamModel);
        UserTeamModel createdUserTeam = userTeamService.createUserTeam(userTeamModel);
        UserTeamResponse userTeamResponse = userTeamDtoMapper.toResponse(createdUserTeam);
        return new ResponseEntity<>(userTeamResponse, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTeamResponse> getUserTeamById(@PathVariable int id) {
        return userTeamService.getUserTeamById(id)
                .map(userTeamDtoMapper::toResponse)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<UserTeamResponse>> getAllUserTeams() {
        List<UserTeamResponse> userTeams = userTeamService.getAllUserTeam().stream()
                .map(userTeamDtoMapper::toResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userTeams, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserTeam(@PathVariable int id, @Valid @RequestBody UserTeamRequest userTeamRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handling validation errors
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            UserResponse userResponse = profileServiceClient.getUserById(userTeamRequest.getUserId()).getBody();
            TeamModel teamModel = teamService.getTeamById(userTeamRequest.getTeam_id()).orElseThrow(()->new TeamBadRequestException(userTeamRequest.getTeam_id()));
            UserTeamModel userTeamModel = userTeamDtoMapper.toModel(userTeamRequest, teamModel);
            UserTeamModel updatedUserTeam = userTeamService.updateUserTeam(id, userTeamModel);
            UserTeamResponse userTeamResponse = userTeamDtoMapper.toResponse(updatedUserTeam);
            return new ResponseEntity<>(userTeamResponse, HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            throw new UserTeamNotFoundException(id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserTeam(@PathVariable int id) {
        userTeamService.getUserTeamById(id).orElseThrow(()->new UserTeamNotFoundException(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
