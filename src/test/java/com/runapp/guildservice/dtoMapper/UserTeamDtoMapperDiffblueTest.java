package com.runapp.guildservice.dtoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.runapp.guildservice.dto.request.UserTeamRequest;
import com.runapp.guildservice.dto.response.UserTeamResponse;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;

import com.runapp.guildservice.staticObject.StaticTeam;
import com.runapp.guildservice.staticObject.StaticUserTeam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserTeamDtoMapper.class})
@ExtendWith(SpringExtension.class)
class UserTeamDtoMapperDiffblueTest {
    @Autowired
    private UserTeamDtoMapper userTeamDtoMapper;

    /**
     * Method under test:
     * {@link UserTeamDtoMapper#toModel(UserTeamRequest, TeamModel)}
     */
    @Test
    void testToModel() {
        UserTeamRequest request = StaticUserTeam.userTeamRequest();

        TeamModel teamModel = StaticTeam.teamModel();
        UserTeamModel actualToModelResult = userTeamDtoMapper.toModel(request, teamModel);
        assertEquals("1", actualToModelResult.getUserId());
        assertSame(teamModel, actualToModelResult.getTeam());
    }

    /**
     * Method under test:
     * {@link UserTeamDtoMapper#toModel(UserTeamRequest, TeamModel)}
     */
    @Test
    void testToModel2() {
        UserTeamRequest request = mock(UserTeamRequest.class);
        when(request.getUserId()).thenReturn("1");

        TeamModel teamModel = StaticTeam.teamModel();
        UserTeamModel actualToModelResult = userTeamDtoMapper.toModel(request, teamModel);
        verify(request).getUserId();
        assertEquals("1", actualToModelResult.getUserId());
        assertSame(teamModel, actualToModelResult.getTeam());
    }

    /**
     * Method under test: {@link UserTeamDtoMapper#toResponse(UserTeamModel)}
     */
    @Test
    void testToResponse() {
        TeamModel team = StaticTeam.teamModel();

        UserTeamModel model = StaticUserTeam.userTeamModel();
        UserTeamResponse actualToResponseResult = userTeamDtoMapper.toResponse(model);
        assertEquals(1, actualToResponseResult.getId());
        assertEquals(1, actualToResponseResult.getTeamId());
        assertEquals("1", actualToResponseResult.getUserId());
    }

    /**
     * Method under test: {@link UserTeamDtoMapper#toResponse(UserTeamModel)}
     */
    @Test
    void testToResponse2() {
        TeamModel team = StaticTeam.teamModel();

        TeamModel teamModel = StaticTeam.teamModel();
        UserTeamModel model = mock(UserTeamModel.class);
        when(model.getTeam()).thenReturn(teamModel);
        when(model.getId()).thenReturn(1);
        when(model.getUserId()).thenReturn("1");
        doNothing().when(model).setId(anyInt());
        doNothing().when(model).setTeam(Mockito.<TeamModel>any());
        doNothing().when(model).setUserId(anyString());
        model.setId(1);
        model.setTeam(team);
        model.setUserId("1");
        UserTeamResponse actualToResponseResult = userTeamDtoMapper.toResponse(model);
        verify(model).getId();
        verify(model).getTeam();
        verify(model).getUserId();
        verify(model).setId(anyInt());
        verify(model).setTeam(Mockito.<TeamModel>any());
        verify(model).setUserId(anyString());
        assertEquals(1, actualToResponseResult.getId());
        assertEquals(1, actualToResponseResult.getTeamId());
        assertEquals("1", actualToResponseResult.getUserId());
    }
}
