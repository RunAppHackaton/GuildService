package com.runapp.guildservice.service;

import com.runapp.guildservice.model.UserTeamModel;
import com.runapp.guildservice.repository.UserTeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserTeamService.class);
    @Autowired
    public UserTeamService(UserTeamRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }

    public UserTeamModel createUserTeam(UserTeamModel userTeam) {
        LOGGER.info("UserTeam add: {}", userTeam);
        return userTeamRepository.save(userTeam);
    }

    public Optional<UserTeamModel> getUserTeamById(int id) {
        LOGGER.info("UserTeam get by id: {}", id);
        return userTeamRepository.findById(id);
    }

    public List<UserTeamModel> getAllUserTeam() {
        LOGGER.info("UserTeam get all");
        return userTeamRepository.findAll();
    }

    public UserTeamModel updateUserTeam(int id, UserTeamModel updatedUserTeam) {
        LOGGER.info("UserTeam update by id: id={}, updatedUserTeam={}", id, updatedUserTeam);
        Optional<UserTeamModel> existingUserTeam = userTeamRepository.findById(id);
        if (existingUserTeam.isPresent()) {
            updatedUserTeam.setId(id);
            return userTeamRepository.save(updatedUserTeam);
        } else {
            throw new IllegalArgumentException("Depend of user with command with ID " + id + " not found.");
        }
    }

    public void deleteUserTeam(int id) {
        LOGGER.info("UserTeam delete by id: {}", id);
        userTeamRepository.deleteById(id);
    }
}
