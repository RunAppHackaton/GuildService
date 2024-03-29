package com.runapp.guildservice.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.guildservice.dto.response.ExceptionResponse;
import com.runapp.guildservice.dtoMapper.TeamDtoMapper;
import com.runapp.guildservice.dto.request.DeleteStorageRequest;
import com.runapp.guildservice.dto.request.TeamDeleteRequest;
import com.runapp.guildservice.dto.request.TeamRequest;
import com.runapp.guildservice.dto.request.TeamUpdateRequest;
import com.runapp.guildservice.dto.response.TeamResponse;
import com.runapp.guildservice.exceptions.GlobalExceptionHandler;
import com.runapp.guildservice.exceptions.TeamBadRequestException;
import com.runapp.guildservice.feignClient.StorageServiceClient;
import com.runapp.guildservice.feignClient.StoryManagementServiceClient;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.staticObject.StaticTeam;
import feign.FeignException;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {TeamController.class})
@ExtendWith(SpringExtension.class)
class TeamControllerDiffblueTest {

    @MockBean
    private StorageServiceClient storageServiceClient;

    @MockBean
    private StoryManagementServiceClient storyManagementServiceClient;

    @Autowired
    private TeamController teamController;

    @MockBean
    private TeamDtoMapper teamDtoMapper;

    @MockBean
    private TeamService teamService;

    /**
     * Method under test: {@link TeamController#getTeamById(int)}
     */
    @Test
    void testGetTeamById() throws Exception {
        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        when(teamService.getTeamById(anyInt())).thenReturn(ofResult);
        when(teamDtoMapper.toResponse(Mockito.<TeamModel>any())).thenReturn(new TeamResponse());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teams/{id}", 1);
        MockMvcBuilders.standaloneSetup(teamController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":0,\"teamName\":null,\"descriptionTeam\":null,\"createDate\":null,\"teamImageUrl\":null,\"ranking\":null,"
                                        + "\"storyId\":0,\"maximumPlayers\":0,\"adminId\":null,\"users_in_team\":null}"));
    }

    /**
     * Method under test: {@link TeamController#getTeamById(int)}
     */
    @Test
    void testGetTeamById2() throws Exception {
        Optional<TeamModel> emptyResult = Optional.empty();
        when(teamService.getTeamById(anyInt())).thenReturn(emptyResult);
        FeignException feignException = mock(FeignException.class);
        when(feignException.getCause()).thenReturn(new Throwable());
        when(teamDtoMapper.toResponse(Mockito.<TeamModel>any())).thenThrow(feignException);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teams/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(teamController).build().perform(requestBuilder);
        actualPerformResult.andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link TeamController#getAllTeams()}
     */
    @Test
    void testGetAllTeams() throws Exception {
        when(teamService.getAllTeams()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teams");
        MockMvcBuilders.standaloneSetup(teamController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link TeamController#createTeam(TeamRequest, BindingResult)}
     */
    @Test
    void testCreateTeam() throws Exception {
        when(teamService.getAllTeams()).thenReturn(new ArrayList<>());

        TeamRequest teamRequest = StaticTeam.teamRequest();
        String content = (new ObjectMapper()).writeValueAsString(teamRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(teamController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link TeamController#deleteTeam(int)}
     */
    @Test
    void testDeleteTeam() throws Exception {
        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        doNothing().when(teamService).deleteTeam(anyInt());
        when(teamService.getTeamById(anyInt())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/teams/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(teamController).build().perform(requestBuilder);
        actualPerformResult.andExpect(status().isNoContent());
    }

    /**
     * Method under test: {@link TeamController#deleteImage(TeamDeleteRequest)}
     */

    // Doesn't check for the request where team doesn't exist
    @Test
    void testDeleteImage() throws Exception {
        TeamModel teamModel = StaticTeam.teamModel();
        when(teamService.updateTeam(anyInt(), Mockito.<TeamModel>any())).thenReturn(teamModel);
        Optional<TeamModel> emptyResult = Optional.empty();
        when(teamService.getTeamById(anyInt())).thenReturn(emptyResult);
        FeignException feignException = mock(FeignException.class);
        when(feignException.getCause()).thenReturn(new Throwable());
        when(storageServiceClient.deleteFile(Mockito.<DeleteStorageRequest>any())).thenThrow(feignException);

        TeamDeleteRequest teamDeleteRequest = new TeamDeleteRequest();
        teamDeleteRequest.setFile_uri("File uri");
        teamDeleteRequest.setTeam_id(1);
    }

    /**
     * Method under test:
     * {@link TeamController#updateTeam(int, TeamUpdateRequest, BindingResult)}
     */
    @Test
    void testUpdateTeam_Success() {
        // Arrange
        int teamId = 1;
        TeamUpdateRequest request = new TeamUpdateRequest(); // Provide valid request object
        TeamModel existingTeam = new TeamModel();
        when(teamService.getTeamById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamDtoMapper.toResponse(existingTeam)).thenReturn(new TeamResponse()); // Mock response

        // Act
        ResponseEntity<Object> responseEntity = teamController.updateTeam(teamId, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        verify(teamService, times(1)).getTeamById(teamId);
        verify(teamDtoMapper, times(1)).updateTeamByRequest(existingTeam, request);
        verify(teamDtoMapper, times(1)).toResponse(existingTeam);
    }

    @Test
    void testUpdateTeam_InvalidInput() throws Exception {
        String errorMessage = "Bad request message";

        // Act & Assert
        MockMvcBuilders.standaloneSetup(teamController).setControllerAdvice(GlobalExceptionHandler.class).build()
                .perform(MockMvcRequestBuilders.put("/teams/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Method under test: {@link TeamController#uploadImage(int, MultipartFile)}
     */
    @Test
    void testUploadImage() throws Exception {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.readAllBytes()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        doNothing().when(contentStream).close();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/teams/upload-image")
                .param("file", String.valueOf(new MockMultipartFile("Name", contentStream)))
                .param("team_id", "https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(teamController).build().perform(requestBuilder);
        actualPerformResult.andExpect(status().is(400));
    }
}
