package com.runapp.guildservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.guildservice.dto.dtoMapper.UserTeamDtoMapper;
import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserResponse;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.feignClient.ProfileServiceClient;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.repository.TeamRepository;
import com.runapp.guildservice.repository.UserTeamRepository;
import com.runapp.guildservice.service.TeamService;
import com.runapp.guildservice.service.UserTeamService;
import feign.FeignException;

import java.time.LocalDate;
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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@ContextConfiguration(classes = {UserTeamController.class})
@ExtendWith(SpringExtension.class)
class UserTeamControllerDiffblueTest {
    @MockBean
    private ProfileServiceClient profileServiceClient;

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
        TeamModel team = new TeamModel();
        team.setAdminId(1);
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId(1);
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);
        when(userTeamService.getUserTeamById(anyInt())).thenReturn(ofResult);
        when(userTeamDtoMapper.toResponse(Mockito.<UserTeamModel>any())).thenReturn(new UserTeamResponse(1, 1, 1));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userteams/{id}", 1);
        MockMvcBuilders.standaloneSetup(userTeamController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"userId\":1,\"teamId\":1}"));
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

        TeamModel team = new TeamModel();
        team.setAdminId(1);
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId(1);
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);

        TeamModel team2 = new TeamModel();
        team2.setAdminId(1);
        team2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team2.setDescriptionTeam("Description Team");
        team2.setId(1);
        team2.setMaximumPlayers(3);
        team2.setRanking(1L);
        team2.setStoryId(1);
        team2.setTeamImageUrl("https://example.org/example");
        team2.setTeamName("Team Name");
        team2.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel2 = new UserTeamModel();
        userTeamModel2.setId(1);
        userTeamModel2.setTeam(team2);
        userTeamModel2.setUserId(1);
        UserTeamRepository userTeamRepository = mock(UserTeamRepository.class);
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenReturn(userTeamModel2);
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        UserTeamService userTeamService = new UserTeamService(userTeamRepository);

        TeamModel teamModel = new TeamModel();
        teamModel.setAdminId(1);
        teamModel.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        teamModel.setDescriptionTeam("Description Team");
        teamModel.setId(1);
        teamModel.setMaximumPlayers(3);
        teamModel.setRanking(1L);
        teamModel.setStoryId(1);
        teamModel.setTeamImageUrl("https://example.org/example");
        teamModel.setTeamName("Team Name");
        teamModel.setUserTeamModelList(new ArrayList<>());
        Optional<TeamModel> ofResult2 = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());
        ProfileServiceClient profileServiceClient = mock(ProfileServiceClient.class);
        when(profileServiceClient.getUserById(anyInt())).thenReturn(responseEntity);
        UserTeamController userTeamController = new UserTeamController(userTeamService, new UserTeamDtoMapper(),
                teamService, profileServiceClient);
        UserTeamRequest userTeamRequest = new UserTeamRequest(1, 1);

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(profileServiceClient).getUserById(anyInt());
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
        verify(responseEntity).getBody();
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getTeamId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getUserId());
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
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "org.springframework.http.ResponseEntity.getBody()" because the return value of "com.runapp.guildservice.feignClient.ProfileServiceClient.getUserById(int)" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.http.ResponseEntity.getBody()" because the return value of "com.runapp.guildservice.feignClient.ProfileServiceClient.getUserById(int)" is null
        //       at com.runapp.guildservice.controller.UserTeamController.updateUserTeam(UserTeamController.java:96)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        TeamModel team = new TeamModel();
        team.setAdminId(1);
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId(1);
        UserTeamService userTeamService = mock(UserTeamService.class);
        when(userTeamService.updateUserTeam(anyInt(), Mockito.<UserTeamModel>any())).thenReturn(userTeamModel);

        TeamModel teamModel = new TeamModel();
        teamModel.setAdminId(1);
        teamModel.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        teamModel.setDescriptionTeam("Description Team");
        teamModel.setId(1);
        teamModel.setMaximumPlayers(3);
        teamModel.setRanking(1L);
        teamModel.setStoryId(1);
        teamModel.setTeamImageUrl("https://example.org/example");
        teamModel.setTeamName("Team Name");
        teamModel.setUserTeamModelList(new ArrayList<>());
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());
        ProfileServiceClient profileServiceClient = mock(ProfileServiceClient.class);
        when(profileServiceClient.getUserById(anyInt())).thenReturn(responseEntity);
        UserTeamController userTeamController = new UserTeamController(userTeamService, new UserTeamDtoMapper(),
                teamService, profileServiceClient);
        UserTeamRequest userTeamRequest = new UserTeamRequest(1, 1);

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(profileServiceClient).getUserById(anyInt());
        verify(userTeamService).updateUserTeam(anyInt(), Mockito.<UserTeamModel>any());
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(responseEntity).getBody();
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getTeamId());
        assertEquals(1, ((UserTeamResponse) actualUpdateUserTeamResult.getBody()).getUserId());
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
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: java.lang.NullPointerException: Cannot invoke "org.springframework.http.ResponseEntity.getBody()" because the return value of "com.runapp.guildservice.feignClient.ProfileServiceClient.getUserById(int)" is null
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.http.ResponseEntity.getBody()" because the return value of "com.runapp.guildservice.feignClient.ProfileServiceClient.getUserById(int)" is null
        //       at com.runapp.guildservice.controller.UserTeamController.updateUserTeam(UserTeamController.java:96)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        TeamModel team = new TeamModel();
        team.setAdminId(1);
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());
        UserTeamModel userTeamModel = mock(UserTeamModel.class);
        doNothing().when(userTeamModel).setId(anyInt());
        doNothing().when(userTeamModel).setTeam(Mockito.<TeamModel>any());
        doNothing().when(userTeamModel).setUserId(anyInt());
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId(1);
        UserTeamService userTeamService = mock(UserTeamService.class);
        when(userTeamService.updateUserTeam(anyInt(), Mockito.<UserTeamModel>any())).thenReturn(userTeamModel);

        TeamModel team2 = new TeamModel();
        team2.setAdminId(1);
        team2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team2.setDescriptionTeam("Description Team");
        team2.setId(1);
        team2.setMaximumPlayers(3);
        team2.setRanking(1L);
        team2.setStoryId(1);
        team2.setTeamImageUrl("https://example.org/example");
        team2.setTeamName("Team Name");
        team2.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel2 = new UserTeamModel();
        userTeamModel2.setId(1);
        userTeamModel2.setTeam(team2);
        userTeamModel2.setUserId(1);
        UserTeamDtoMapper userTeamDtoMapper = mock(UserTeamDtoMapper.class);
        when(userTeamDtoMapper.toResponse(Mockito.<UserTeamModel>any())).thenReturn(new UserTeamResponse(1, 1, 1));
        when(userTeamDtoMapper.toModel(Mockito.<UserTeamRequest>any(), Mockito.<TeamModel>any()))
                .thenReturn(userTeamModel2);

        TeamModel teamModel = new TeamModel();
        teamModel.setAdminId(1);
        teamModel.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        teamModel.setDescriptionTeam("Description Team");
        teamModel.setId(1);
        teamModel.setMaximumPlayers(3);
        teamModel.setRanking(1L);
        teamModel.setStoryId(1);
        teamModel.setTeamImageUrl("https://example.org/example");
        teamModel.setTeamName("Team Name");
        teamModel.setUserTeamModelList(new ArrayList<>());
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TeamService teamService = new TeamService(teamRepository);
        ResponseEntity<UserResponse> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(new UserResponse());
        ProfileServiceClient profileServiceClient = mock(ProfileServiceClient.class);
        when(profileServiceClient.getUserById(anyInt())).thenReturn(responseEntity);
        UserTeamController userTeamController = new UserTeamController(userTeamService, userTeamDtoMapper, teamService,
                profileServiceClient);
        UserTeamRequest userTeamRequest = new UserTeamRequest(1, 1);

        ResponseEntity<Object> actualUpdateUserTeamResult = userTeamController.updateUserTeam(1, userTeamRequest);
        verify(userTeamDtoMapper).toModel(Mockito.<UserTeamRequest>any(), Mockito.<TeamModel>any());
        verify(userTeamDtoMapper).toResponse(Mockito.<UserTeamModel>any());
        verify(profileServiceClient).getUserById(anyInt());
        verify(userTeamModel).setId(anyInt());
        verify(userTeamModel).setTeam(Mockito.<TeamModel>any());
        verify(userTeamModel).setUserId(anyInt());
        verify(userTeamService).updateUserTeam(anyInt(), Mockito.<UserTeamModel>any());
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(responseEntity).getBody();
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

        UserTeamRequest userTeamRequest = new UserTeamRequest();
        userTeamRequest.setTeam_id(1);
        userTeamRequest.setUserId(1);
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
        TeamModel team = new TeamModel();
        team.setAdminId(1);
        team.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        team.setDescriptionTeam("Description Team");
        team.setId(1);
        team.setMaximumPlayers(3);
        team.setRanking(1L);
        team.setStoryId(1);
        team.setTeamImageUrl("https://example.org/example");
        team.setTeamName("Team Name");
        team.setUserTeamModelList(new ArrayList<>());

        UserTeamModel userTeamModel = new UserTeamModel();
        userTeamModel.setId(1);
        userTeamModel.setTeam(team);
        userTeamModel.setUserId(1);
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
