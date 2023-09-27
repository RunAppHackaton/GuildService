package com.runapp.guildservice.repository;

import com.runapp.guildservice.model.TeamMembershipRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembershipRequestRepository extends JpaRepository<TeamMembershipRequestModel, Long> {
}
