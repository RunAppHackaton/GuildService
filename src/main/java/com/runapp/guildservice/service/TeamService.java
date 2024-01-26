package com.runapp.guildservice.service;

import com.runapp.guildservice.model.TeamModel;
import com.runapp.guildservice.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public TeamModel createTeam(TeamModel team) {
        LOGGER.info("Team add: {}", team);
        return teamRepository.save(team);
    }

    public Optional<TeamModel> getTeamById(int id) {
        LOGGER.info("Team get by id: {}", id);
        return teamRepository.findById(id);
    }

    public List<TeamModel> getAllTeams() {
        LOGGER.info("Team get all");
        return teamRepository.findAll();
    }

    public TeamModel updateTeam(int id, TeamModel updatedTeam) {
        LOGGER.info("Team update: id={}, updatedTeam={}", id, updatedTeam);
        Optional<TeamModel> existingTeam = teamRepository.findById(id);
        if (existingTeam.isPresent()) {
            updatedTeam.setId(id);
            return teamRepository.save(updatedTeam);
        } else {
            throw new IllegalArgumentException("Team with ID " + id + " not found.");
        }
    }

    public void deleteTeam(int id) {
        LOGGER.info("Team delete by id: {}", id);
        teamRepository.deleteById(id);
    }
}
