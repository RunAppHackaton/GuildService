package com.runapp.guildservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.guildservice.dtoMapper.UserTeamDtoMapper;
import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserResponse;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.repository.TeamRepository;
import com.runapp.guildservice.repository.UserTeamRepository;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.service.UserTeamService;
import com.runapp.guildservice.staticObject.StaticTeam;
import com.runapp.guildservice.staticObject.StaticUserTeam;
import feign.FeignException;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

@ContextConfiguration(classes = {UserTeamController.class})
@ExtendWith(SpringExtension.class)
class UserTeamControllerDiffblueTest {

    @MockBean
    private TeamService teamService;

    @Autowired
    private UserTeamController userTeamController;

    @MockBean
    private UserTeamDtoMapper userTeamDtoMapper;

    @MockBean
    private UserTeamService userTeamService;

    /**
     * Method under test: {@link UserTeamController#getUserTeamById(int)}
     */
    @Test
    void testGetUserTeamById() throws Exception {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);
        when(userTeamService.getUserTeamById(anyInt())).thenReturn(ofResult);
        when(userTeamDtoMapper.toResponse(Mockito.<UserTeamModel>any())).thenReturn(StaticUserTeam.userTeamResponse());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userteams/{id}", 1);
        MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"userId\":\"1\",\"teamId\":1}"));
    }

    /**
     * Method under test: {@link UserTeamController#getUserTeamById(int)}
     */
    @Test
    void testGetUserTeamById2() throws Exception {
        Optional<UserTeamModel> emptyResult = Optional.empty();
        when(userTeamService.getUserTeamById(anyInt())).thenReturn(emptyResult);
        FeignException feignException = mock(FeignException.class);
        when(feignException.getCause()).thenReturn(new Throwable());
        when(userTeamDtoMapper.toResponse(Mockito.<UserTeamModel>any())).thenThrow(feignException);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userteams/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test:
     * {@link UserTeamController#updateUserTeam(int, UserTeamRequest, BindingResult)}
     */
    @Test
    void testUpdateUserTeam() {

        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);

        UserTeamModel userTeamModel2 = StaticUserTeam.userTeamModel();
        UserTeamRepository userTeamRepository = mock(UserTeamRepository.class);
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenReturn(userTeamModel2);
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        UserTeamService userTeamService = new UserTeamService(userTeamRepository);

        TeamModel teamModel = StaticTeam.teamModel2();
        Optional<TeamModel> ofResult2 = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());
        UserTeamController userTeamController = new UserTeamController(userTeamService, new UserTeamDtoMapper(),
                teamService);
        UserTeamRequest userTeamRequest = new UserTeamRequest("1", 1);

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getTeamId());
        assertEquals("1", ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getUserId());
        assertEquals(200, actualUpdateUserTeamResult.getStatusCodeValue());
        assertTrue(actualUpdateUserTeamResult.hasBody());
        assertTrue(actualUpdateUserTeamResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link UserTeamController#updateUserTeam(Long, UserTeamRequest, BindingResult)}
     */
    @Test
    void testUpdateUserTeam2() {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        UserTeamService userTeamService = mock(UserTeamService.class);
        when(userTeamService.updateUserTeam(anyInt(), Mockito.<UserTeamModel>any())).thenReturn(userTeamModel);

        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());

        UserTeamController userTeamController = new UserTeamController(userTeamService, new UserTeamDtoMapper(),
                teamService);
        UserTeamRequest userTeamRequest = StaticUserTeam.userTeamRequest();

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(userTeamService).updateUserTeam(anyInt(), Mockito.<UserTeamModel>any());
        verify(teamRepository).findById(Mockito.<Integer>any());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getTeamId());
        assertEquals("1", ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getUserId());
        assertEquals(200, actualUpdateUserTeamResult.getStatusCodeValue());
        assertTrue(actualUpdateUserTeamResult.hasBody());
        assertTrue(actualUpdateUserTeamResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link UserTeamController#updateUserTeam(int, UserTeamRequest, BindingResult)}
     */
    @Test
    void testUpdateUserTeam3() {
        TeamModel team = StaticTeam.teamModel();
        UserTeamModel userTeamModel = mock(UserTeamModel.class);
        doNothing().when(userTeamModel).setId(anyInt());
        doNothing().when(userTeamModel).setTeam(Mockito.<TeamModel>any());
        doNothing().when(userTeamModel).setUserId(anyString());
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId("1");
        UserTeamService userTeamService = mock(UserTeamService.class);
        when(userTeamService.updateUserTeam(anyInt(), Mockito.<UserTeamModel>any())).thenReturn(userTeamModel);

        UserTeamModel userTeamModel2 = StaticUserTeam.userTeamModel();
        UserTeamDtoMapper userTeamDtoMapper = mock(UserTeamDtoMapper.class);
        when(userTeamDtoMapper.toResponse(Mockito.<UserTeamModel>any())).thenReturn(new UserTeamResponse(1, "1", 1));
        when(userTeamDtoMapper.toModel(Mockito.<UserTeamRequest>any(), Mockito.<TeamModel>any()))
                .thenReturn(userTeamModel2);

        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());

        UserTeamController userTeamController = new UserTeamController(userTeamService, userTeamDtoMapper, teamService);
        UserTeamRequest userTeamRequest = StaticUserTeam.userTeamRequest();

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(userTeamDtoMapper).toModel(Mockito.<UserTeamRequest>any(), Mockito.<TeamModel>any());
        verify(userTeamDtoMapper).toResponse(Mockito.<UserTeamModel>any());

        verify(userTeamModel).setId(anyInt());
        verify(userTeamModel).setTeam(Mockito.<TeamModel>any());
        verify(userTeamModel).setUserId(anyString());
        verify(userTeamService).updateUserTeam(anyInt(), Mockito.<UserTeamModel>any());
        verify(teamRepository).findById(Mockito.<Integer>any());
        assertEquals(200, actualUpdateUserTeamResult.getStatusCodeValue());
        assertTrue(actualUpdateUserTeamResult.hasBody());
        assertTrue(actualUpdateUserTeamResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link UserTeamController#createUserTeam(UserTeamRequest, BindingResult)}
     */
    @Test
    void testCreateUserTeam() throws Exception {
        when(userTeamService.getAllUserTeam()).thenReturn(new ArrayList<>());

        UserTeamRequest userTeamRequest = StaticUserTeam.userTeamRequest();
        String content = (new ObjectMapper()).writeValueAsString(userTeamRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userteams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserTeamController#deleteUserTeam(int)}
     */
    @Test
    void testDeleteUserTeam() throws Exception {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);
        doNothing().when(userTeamService).deleteUserTeam(anyInt());
        when(userTeamService.getUserTeamById(anyInt())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/userteams/{id}", 1);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link UserTeamController#getAllUserTeams()}
     */
    @Test
    void testGetAllUserTeams() throws Exception {
        when(userTeamService.getAllUserTeam()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userteams");
        MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
