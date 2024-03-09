package com.runapp.guildservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.guildservice.dtoMapper.TeamDtoMapper;
import com.runapp.guildservice.dto.request.TeamRequest;
import com.runapp.guildservice.dto.request.TeamUpdateRequest;
import com.runapp.guildservice.dto.response.TeamResponse;
import com.runapp.guildservice.exceptions.GlobalExceptionHandler;
import com.runapp.guildservice.feignClient.StoryManagementServiceClient;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.staticObject.StaticTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private TeamDtoMapper teamDtoMapper;

    @Mock
    private StoryManagementServiceClient storyManagementServiceClient;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    public void testUpdateTeamWhenNonIntegerIdThenReturnBadRequest() throws Exception {
        TeamUpdateRequest teamUpdateRequest = StaticTeam.teamUpdateRequest();
        mockMvc.perform(MockMvcRequestBuilders.put("/teams/invalid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamUpdateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTeamWhenValidationErrorsThenReturnBadRequest() throws Exception {
        TeamUpdateRequest teamUpdateRequest = StaticTeam.teamUpdateRequestBad();

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamUpdateRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateTeamWhenInvalidRequestThenBadRequest() throws Exception {
        TeamRequest teamRequest = StaticTeam.teamRequestBad();

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteTeamWhenTeamExistsThenTeamDeleted() throws Exception {
        int teamId = 1;
        TeamModel teamModel = StaticTeam.teamModel2();

        when(teamService.getTeamById(teamId)).thenReturn(Optional.of(teamModel));

        mockMvc.perform(delete("/teams/" + teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTeamByIdWhenTeamFoundThenReturnTeamResponse() throws Exception {
        TeamModel teamModel = StaticTeam.teamModel2();
        TeamResponse teamResponse = StaticTeam.teamResponse2();

        when(teamService.getTeamById(anyInt())).thenReturn(Optional.of(teamModel));
        when(teamDtoMapper.toResponse(any(TeamModel.class))).thenReturn(teamResponse);

        mockMvc.perform(get("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teamResponse)));
    }

    @Test
    public void testGetTeamByIdWhenTeamNotFoundThenReturnNotFound() throws Exception {
        when(teamService.getTeamById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTeamByIdWhenInvalidIdThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/teams/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateTeamWhenInvalidRequestThenBadRequest() throws Exception {
        TeamUpdateRequest teamUpdateRequest = StaticTeam.teamUpdateRequest();

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamUpdateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllTeamsWhenValidParametersThenSuccess() throws Exception {
        TeamModel teamModel = StaticTeam.teamModel2();
        TeamResponse teamResponse = StaticTeam.teamResponse2();
        List<TeamModel> teamModels = Arrays.asList(teamModel);
        List<TeamResponse> teamResponses = Arrays.asList(teamResponse);

        when(teamService.getAllTeams()).thenReturn(teamModels);
        when(teamDtoMapper.toResponse(any(TeamModel.class))).thenReturn(teamResponse);

        mockMvc.perform(get("/teams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teamResponses)));
    }
}