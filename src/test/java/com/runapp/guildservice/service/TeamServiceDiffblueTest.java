package com.runapp.guildservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.repository.TeamRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.runapp.guildservice.staticObject.StaticTeam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TeamService.class})
@ExtendWith(SpringExtension.class)
class TeamServiceDiffblueTest {
    @MockBean
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    /**
     * Method under test: {@link TeamService#createTeam(TeamModel)}
     */
    @Test
    void testCreateTeam() {
        TeamModel teamModel = StaticTeam.teamModel();
        when(teamRepository.save(Mockito.<TeamModel>any())).thenReturn(teamModel);

        TeamModel team = StaticTeam.teamModel();
        TeamModel actualCreateTeamResult = teamService.createTeam(team);
        verify(teamRepository).save(Mockito.<TeamModel>any());
        assertSame(teamModel, actualCreateTeamResult);
    }

    /**
     * Method under test: {@link TeamService#createTeam(TeamModel)}
     */
    @Test
    void testCreateTeam2() {
        when(teamRepository.save(Mockito.<TeamModel>any())).thenThrow(new IllegalArgumentException("foo"));

        TeamModel team = StaticTeam.teamModel();
        assertThrows(IllegalArgumentException.class, () -> teamService.createTeam(team));
        verify(teamRepository).save(Mockito.<TeamModel>any());
    }

    /**
     * Method under test: {@link TeamService#getTeamById(int)}
     */
    @Test
    void testGetTeamById() {
        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<TeamModel> actualTeamById = teamService.getTeamById(1);
        verify(teamRepository).findById(Mockito.<Integer>any());
        assertTrue(actualTeamById.isPresent());
        assertSame(ofResult, actualTeamById);
    }

    /**
     * Method under test: {@link TeamService#getTeamById(int)}
     */
    @Test
    void testGetTeamById2() {
        when(teamRepository.findById(Mockito.<Integer>any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> teamService.getTeamById(1));
        verify(teamRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link TeamService#getAllTeams()}
     */
    @Test
    void testGetAllTeams() {
        ArrayList<TeamModel> teamModelList = new ArrayList<>();
        when(teamRepository.findAll()).thenReturn(teamModelList);
        List<TeamModel> actualAllTeams = teamService.getAllTeams();
        verify(teamRepository).findAll();
        assertTrue(actualAllTeams.isEmpty());
        assertSame(teamModelList, actualAllTeams);
    }

    /**
     * Method under test: {@link TeamService#getAllTeams()}
     */
    @Test
    void testGetAllTeams2() {
        when(teamRepository.findAll()).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> teamService.getAllTeams());
        verify(teamRepository).findAll();
    }

    /**
     * Method under test: {@link TeamService#updateTeam(int, TeamModel)}
     */
    @Test
    void testUpdateTeam() {
        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);

        TeamModel teamModel2 = StaticTeam.teamModel();
        when(teamRepository.save(Mockito.<TeamModel>any())).thenReturn(teamModel2);
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        TeamModel updatedTeam = StaticTeam.teamModel();
        TeamModel actualUpdateTeamResult = teamService.updateTeam(1, updatedTeam);
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(teamRepository).save(Mockito.<TeamModel>any());
        assertEquals(1, updatedTeam.getId());
        assertSame(teamModel2, actualUpdateTeamResult);
    }

    /**
     * Method under test: {@link TeamService#updateTeam(int, TeamModel)}
     */
    @Test
    void testUpdateTeam2() {
        TeamModel teamModel = StaticTeam.teamModel();
        Optional<TeamModel> ofResult = Optional.of(teamModel);
        when(teamRepository.save(Mockito.<TeamModel>any())).thenThrow(new IllegalArgumentException("foo"));
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        TeamModel updatedTeam = StaticTeam.teamModel();
        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(1, updatedTeam));
        verify(teamRepository).findById(Mockito.<Integer>any());
        verify(teamRepository).save(Mockito.<TeamModel>any());
    }

    /**
     * Method under test: {@link TeamService#updateTeam(int, TeamModel)}
     */
    @Test
    void testUpdateTeam3() {
        Optional<TeamModel> emptyResult = Optional.empty();
        when(teamRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        TeamModel updatedTeam = StaticTeam.teamModel();
        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(1, updatedTeam));
        verify(teamRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link TeamService#deleteTeam(int)}
     */
    @Test
    void testDeleteTeam() {
        doNothing().when(teamRepository).deleteById(Mockito.<Integer>any());
        teamService.deleteTeam(1);
        verify(teamRepository).deleteById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link TeamService#deleteTeam(int)}
     */
    @Test
    void testDeleteTeam2() {
        doThrow(new IllegalArgumentException("foo")).when(teamRepository).deleteById(Mockito.<Integer>any());
        assertThrows(IllegalArgumentException.class, () -> teamService.deleteTeam(1));
        verify(teamRepository).deleteById(Mockito.<Integer>any());
    }
}
