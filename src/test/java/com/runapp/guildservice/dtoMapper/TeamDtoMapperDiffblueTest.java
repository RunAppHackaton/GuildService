package com.runapp.guildservice.dtoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.runapp.guildservice.dto.request.TeamRequest;
import com.runapp.guildservice.dto.response.TeamResponse;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.runapp.guildservice.staticObject.StaticTeam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TeamDtoMapper.class})
@ExtendWith(SpringExtension.class)
class TeamDtoMapperDiffblueTest {
    @Autowired
    private TeamDtoMapper teamDtoMapper;

    /**
     * Method under test: {@link TeamDtoMapper#toModel(TeamRequest)}
     */
    @Test
    void testToModel() {
        TeamModel actualToModelResult = teamDtoMapper.toModel(StaticTeam.teamRequest());
        assertEquals("DEFAULT", actualToModelResult.getTeamImageUrl());
        assertEquals("Description Team", actualToModelResult.getDescriptionTeam());
        assertEquals("Team Name", actualToModelResult.getTeamName());
        assertEquals(0L, actualToModelResult.getRanking().longValue());
        assertEquals("1", actualToModelResult.getAdminId());
        assertEquals(1, actualToModelResult.getStoryId());
        assertEquals(3, actualToModelResult.getMaximumPlayers());
    }

    /**
     * Method under test: {@link TeamDtoMapper#toModel(TeamRequest)}
     */
    @Test
    void testToModel2() {
        TeamRequest teamRequest = mock(TeamRequest.class);
        when(teamRequest.getAdminId()).thenReturn("1");
        when(teamRequest.getMaximumPlayers()).thenReturn(3);
        when(teamRequest.getStoryId()).thenReturn(1);
        when(teamRequest.getDescriptionTeam()).thenReturn("Description Team");
        when(teamRequest.getTeamName()).thenReturn("Team Name");
        TeamModel actualToModelResult = teamDtoMapper.toModel(teamRequest);
        verify(teamRequest).getAdminId();
        verify(teamRequest).getDescriptionTeam();
        verify(teamRequest).getMaximumPlayers();
        verify(teamRequest).getStoryId();
        verify(teamRequest).getTeamName();
        assertEquals("DEFAULT", actualToModelResult.getTeamImageUrl());
        assertEquals("Description Team", actualToModelResult.getDescriptionTeam());
        assertEquals("Team Name", actualToModelResult.getTeamName());
        assertEquals(0L, actualToModelResult.getRanking().longValue());
        assertEquals("1", actualToModelResult.getAdminId());
        assertEquals(1, actualToModelResult.getStoryId());
        assertEquals(3, actualToModelResult.getMaximumPlayers());
    }

    /**
     * Method under test: {@link TeamDtoMapper#toResponse(TeamModel)}
     */
    @Test
    void testToResponse() {
        TeamModel teamModel = StaticTeam.teamModel();
        TeamResponse actualToResponseResult = teamDtoMapper.toResponse(teamModel);
        assertEquals("1970-01-01", actualToResponseResult.getCreateDate().toLocalDate().toString());
        assertEquals("Description Team", actualToResponseResult.getDescriptionTeam());
        assertEquals("Team Name", actualToResponseResult.getTeamName());
        assertEquals("https://example.org/example", actualToResponseResult.getTeamImageUrl());
        assertEquals("1", actualToResponseResult.getAdminId());
        assertEquals(1, actualToResponseResult.getId());
        assertEquals(1, actualToResponseResult.getStoryId());
        assertEquals(1L, actualToResponseResult.getRanking().longValue());
        assertEquals(3, actualToResponseResult.getMaximumPlayers());
        assertTrue(actualToResponseResult.getUsers_in_team().isEmpty());
    }

    /**
     * Method under test: {@link TeamDtoMapper#toResponse(TeamModel)}
     */
    @Test
    void testToResponse2() {
        TeamModel teamModel = mock(TeamModel.class);
        when(teamModel.getAdminId()).thenReturn("1");
        when(teamModel.getId()).thenReturn(1);
        when(teamModel.getMaximumPlayers()).thenReturn(3);
        when(teamModel.getStoryId()).thenReturn(1);
        when(teamModel.getRanking()).thenReturn(1L);
        when(teamModel.getDescriptionTeam()).thenReturn("Description Team");
        when(teamModel.getTeamImageUrl()).thenReturn("https://example.org/example");
        when(teamModel.getTeamName()).thenReturn("Team Name");
        when(teamModel.getCreateDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(teamModel.getUserTeamModelList()).thenReturn(new ArrayList<>());
        doNothing().when(teamModel).setAdminId(anyString());
        doNothing().when(teamModel).setCreateDate(Mockito.<LocalDateTime>any());
        doNothing().when(teamModel).setDescriptionTeam(Mockito.<String>any());
        doNothing().when(teamModel).setId(anyInt());
        doNothing().when(teamModel).setMaximumPlayers(anyInt());
        doNothing().when(teamModel).setRanking(Mockito.<Long>any());
        doNothing().when(teamModel).setStoryId(anyInt());
        doNothing().when(teamModel).setTeamImageUrl(Mockito.<String>any());
        doNothing().when(teamModel).setTeamName(Mockito.<String>any());
        doNothing().when(teamModel).setUserTeamModelList(Mockito.<List<UserTeamModel>>any());
        teamModel.setAdminId("1");
        teamModel.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        teamModel.setDescriptionTeam("Description Team");
        teamModel.setId(1);
        teamModel.setMaximumPlayers(3);
        teamModel.setRanking(1L);
        teamModel.setStoryId(1);
        teamModel.setTeamImageUrl("https://example.org/example");
        teamModel.setTeamName("Team Name");
        teamModel.setUserTeamModelList(new ArrayList<>());
        TeamResponse actualToResponseResult = teamDtoMapper.toResponse(teamModel);
        verify(teamModel).getAdminId();
        verify(teamModel).getCreateDate();
        verify(teamModel).getDescriptionTeam();
        verify(teamModel).getId();
        verify(teamModel).getMaximumPlayers();
        verify(teamModel).getRanking();
        verify(teamModel).getStoryId();
        verify(teamModel).getTeamImageUrl();
        verify(teamModel).getTeamName();
        verify(teamModel).getUserTeamModelList();
        verify(teamModel).setAdminId(anyString());
        verify(teamModel).setCreateDate(Mockito.<LocalDateTime>any());
        verify(teamModel).setDescriptionTeam(Mockito.<String>any());
        verify(teamModel).setId(anyInt());
        verify(teamModel).setMaximumPlayers(anyInt());
        verify(teamModel).setRanking(Mockito.<Long>any());
        verify(teamModel).setStoryId(anyInt());
        verify(teamModel).setTeamImageUrl(Mockito.<String>any());
        verify(teamModel).setTeamName(Mockito.<String>any());
        verify(teamModel).setUserTeamModelList(Mockito.<List<UserTeamModel>>any());
        assertEquals("1970-01-01", actualToResponseResult.getCreateDate().toLocalDate().toString());
        assertEquals("Description Team", actualToResponseResult.getDescriptionTeam());
        assertEquals("Team Name", actualToResponseResult.getTeamName());
        assertEquals("https://example.org/example", actualToResponseResult.getTeamImageUrl());
        assertEquals("1", actualToResponseResult.getAdminId());
        assertEquals(1, actualToResponseResult.getId());
        assertEquals(1, actualToResponseResult.getStoryId());
        assertEquals(1L, actualToResponseResult.getRanking().longValue());
        assertEquals(3, actualToResponseResult.getMaximumPlayers());
        assertTrue(actualToResponseResult.getUsers_in_team().isEmpty());
    }
}
