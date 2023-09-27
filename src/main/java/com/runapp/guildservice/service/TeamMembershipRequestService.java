package com.runapp.guildservice.service;

import com.runapp.guildservice.dto.request.RespondToRequest;
import com.runapp.guildservice.dto.response.ExceptionResponse;
import com.runapp.guildservice.dto.response.UserResponse;
import com.runapp.guildservice.feignClient.ProfileServiceClient;
import com.runapp.guildservice.model.TeamMembershipRequestModel;
import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.repository.TeamMembershipRequestRepository;
import com.runapp.guildservice.repository.TeamRepository;
import com.runapp.guildservice.repository.UserTeamRepository;
import com.runapp.guildservice.utill.RequestStatusEnum;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeamMembershipRequestService {

    private final TeamRepository teamRepository;
    private final TeamMembershipRequestRepository teamMembershipRequestRepository;
    private final ProfileServiceClient profileServiceClient;
    private final UserTeamRepository userTeamRepository;

    @Autowired
    public TeamMembershipRequestService(TeamRepository teamRepository, TeamMembershipRequestRepository teamMembershipRequestRepository, ProfileServiceClient profileServiceClient, UserTeamRepository userTeamRepository) {
        this.teamRepository = teamRepository;
        this.teamMembershipRequestRepository = teamMembershipRequestRepository;
        this.profileServiceClient = profileServiceClient;
        this.userTeamRepository = userTeamRepository;
    }

    public ResponseEntity<Object> createResponseToTeam(int userId, int teamId) {
        try {
            UserResponse userResponse = profileServiceClient.getUserById(userId).getBody();
            TeamMembershipRequestModel model = new TeamMembershipRequestModel();
            if (!teamRepository.existsById(teamId))
                return ResponseEntity.badRequest().body("team with id " + teamId + " does not exist");
            model.setTeamId(teamId);
            model.setUser_id(Long.valueOf(userId));
            model.setRequestDate(LocalDateTime.now());
            model.setStatus(RequestStatusEnum.PENDING);
            teamMembershipRequestRepository.save(model);
            return ResponseEntity.ok().body(model);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(LocalDateTime.now(), "User with ID " + userId + " not found"));
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(LocalDateTime.now(), "No such history or user exists"));
        }
    }

    public ResponseEntity<List<TeamMembershipRequestModel>> getAllRequestsToTeams() {
        return ResponseEntity.ok().body(teamMembershipRequestRepository.findAll());
    }

    public ResponseEntity<Object> acceptRequestByAdministrator(RespondToRequest respondToRequest) {
        try {
            UserResponse userResponse = profileServiceClient.getUserById(respondToRequest.getAdmin_id()).getBody();
            Optional<TeamModel> teamModelOptional = teamRepository.findById(respondToRequest.getTeam_id());
            if (teamModelOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("team with id " + respondToRequest.getAdmin_id() + " does not exist");
            }
            TeamModel teamModel = teamModelOptional.orElse(null);
            if (teamModel.getAdminId() != respondToRequest.getAdmin_id())
                return ResponseEntity.badRequest().body("this admin is not the main one in this group");
            Optional<TeamMembershipRequestModel> teamMembershipRequestModel = teamMembershipRequestRepository.findById(respondToRequest.getRequest_id());
            if (teamMembershipRequestModel.isEmpty())
                return ResponseEntity.badRequest().body("team request with id " + respondToRequest.getRequest_id() + " does not exist");
            TeamMembershipRequestModel model = teamMembershipRequestModel.orElse(null);
            model.setStatus(respondToRequest.getDecision());
            if (model.getStatus().equals(RequestStatusEnum.APPROVED))
                userTeamRepository.save(new UserTeamModel(model.getUser_id().intValue(), new TeamModel(model.getTeamId())));
            teamMembershipRequestRepository.save(model);
            return ResponseEntity.ok().body(model);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(LocalDateTime.now(), "User with ID " + respondToRequest.getAdmin_id() + " not found"));
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(LocalDateTime.now(), "No such history or user exists"));
        }
    }
}