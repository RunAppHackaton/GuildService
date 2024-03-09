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
import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.repository.UserTeamRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.runapp.guildservice.staticObject.StaticTeam;
import com.runapp.guildservice.staticObject.StaticUserTeam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserTeamService.class})
@ExtendWith(SpringExtension.class)
class UserTeamServiceDiffblueTest {
    @MockBean
    private UserTeamRepository userTeamRepository;

    @Autowired
    private UserTeamService userTeamService;

    /**
     * Method under test: {@link UserTeamService#createUserTeam(UserTeamModel)}
     */
    @Test
    void testCreateUserTeam() {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenReturn(userTeamModel);

        UserTeamModel userTeam = StaticUserTeam.userTeamModel();
        UserTeamModel actualCreateUserTeamResult = userTeamService.createUserTeam(userTeam);
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
        assertSame(userTeamModel, actualCreateUserTeamResult);
    }

    /**
     * Method under test: {@link UserTeamService#createUserTeam(UserTeamModel)}
     */
    @Test
    void testCreateUserTeam2() {
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenThrow(new IllegalArgumentException("foo"));

        UserTeamModel userTeam = StaticUserTeam.userTeamModel();
        assertThrows(IllegalArgumentException.class, () -> userTeamService.createUserTeam(userTeam));
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
    }

    /**
     * Method under test: {@link UserTeamService#getUserTeamById(int)}
     */
    @Test
    void testGetUserTeamById() {
        UserTeamModel userTeam = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeam);
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<UserTeamModel> actualUserTeamById = userTeamService.getUserTeamById(1);
        verify(userTeamRepository).findById(Mockito.<Integer>any());
        assertTrue(actualUserTeamById.isPresent());
        assertSame(ofResult, actualUserTeamById);
    }

    /**
     * Method under test: {@link UserTeamService#getUserTeamById(int)}
     */
    @Test
    void testGetUserTeamById2() {
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> userTeamService.getUserTeamById(1));
        verify(userTeamRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link UserTeamService#getAllUserTeam()}
     */
    @Test
    void testGetAllUserTeam() {
        ArrayList<UserTeamModel> userTeamModelList = new ArrayList<>();
        when(userTeamRepository.findAll()).thenReturn(userTeamModelList);
        List<UserTeamModel> actualAllUserTeam = userTeamService.getAllUserTeam();
        verify(userTeamRepository).findAll();
        assertTrue(actualAllUserTeam.isEmpty());
        assertSame(userTeamModelList, actualAllUserTeam);
    }

    /**
     * Method under test: {@link UserTeamService#getAllUserTeam()}
     */
    @Test
    void testGetAllUserTeam2() {
        when(userTeamRepository.findAll()).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> userTeamService.getAllUserTeam());
        verify(userTeamRepository).findAll();
    }

    /**
     * Method under test: {@link UserTeamService#updateUserTeam(int, UserTeamModel)}
     */
    @Test
    void testUpdateUserTeam() {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);

        UserTeamModel userTeamModel2 = StaticUserTeam.userTeamModel();
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenReturn(userTeamModel2);
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        UserTeamModel updatedUserTeam = StaticUserTeam.userTeamModel();
        UserTeamModel actualUpdateUserTeamResult = userTeamService.updateUserTeam(1, updatedUserTeam);
        verify(userTeamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
        assertEquals(1, updatedUserTeam.getId());
        assertSame(userTeamModel2, actualUpdateUserTeamResult);
    }

    /**
     * Method under test: {@link UserTeamService#updateUserTeam(int, UserTeamModel)}
     */
    @Test
    void testUpdateUserTeam2() {
        UserTeamModel userTeamModel = StaticUserTeam.userTeamModel();
        Optional<UserTeamModel> ofResult = Optional.of(userTeamModel);
        when(userTeamRepository.save(Mockito.<UserTeamModel>any())).thenThrow(new IllegalArgumentException("foo"));
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        UserTeamModel updatedUserTeam = StaticUserTeam.userTeamModel();
        assertThrows(IllegalArgumentException.class, () -> userTeamService.updateUserTeam(1, updatedUserTeam));
        verify(userTeamRepository).findById(Mockito.<Integer>any());
        verify(userTeamRepository).save(Mockito.<UserTeamModel>any());
    }

    /**
     * Method under test: {@link UserTeamService#updateUserTeam(int, UserTeamModel)}
     */
    @Test
    void testUpdateUserTeam3() {
        Optional<UserTeamModel> emptyResult = Optional.empty();
        when(userTeamRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        UserTeamModel updatedUserTeam = StaticUserTeam.userTeamModel();
        assertThrows(IllegalArgumentException.class, () -> userTeamService.updateUserTeam(1, updatedUserTeam));
        verify(userTeamRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link UserTeamService#deleteUserTeam(int)}
     */
    @Test
    void testDeleteUserTeam() {
        doNothing().when(userTeamRepository).deleteById(Mockito.<Integer>any());
        userTeamService.deleteUserTeam(1);
        verify(userTeamRepository).deleteById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link UserTeamService#deleteUserTeam(int)}
     */
    @Test
    void testDeleteUserTeam2() {
        doThrow(new IllegalArgumentException("foo")).when(userTeamRepository).deleteById(Mockito.<Integer>any());
        assertThrows(IllegalArgumentException.class, () -> userTeamService.deleteUserTeam(1));
        verify(userTeamRepository).deleteById(Mockito.<Integer>any());
    }
}
