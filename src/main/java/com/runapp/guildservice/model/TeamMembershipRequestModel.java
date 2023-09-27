package com.runapp.guildservice.model;

import com.runapp.guildservice.utill.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_membership_requests")
public class TeamMembershipRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "team_id")
    private int teamId;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatusEnum status;

    public TeamMembershipRequestModel(Long user_id, LocalDateTime requestDate, RequestStatusEnum status) {
        this.user_id = user_id;
        this.requestDate = requestDate;
        this.status = status;
    }
}
