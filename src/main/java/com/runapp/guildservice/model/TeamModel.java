package com.runapp.guildservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Team")
public class TeamModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "description_team")
    private String descriptionTeam;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "team_image_url")
    private String teamImageUrl;

    @Column(name = "ranking")
    private Long ranking;

    @Column(name = "story_id")
    private int storyId;

    @Column(name = "maximum_number_of_players_in_team")
    private int maximumPlayers;

    @Column(name = "admin_id")
    private int adminId;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTeamModel> userTeamModelList;

    public TeamModel(int id) {
        this.id = id;
    }
}
