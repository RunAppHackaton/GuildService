package com.runapp.guildservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;

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

    @NotEmpty(message = "Team name cannot be empty")
    @Length(min = 3, max = 15, message = "Team name must be between 3 and 15 characters")
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

    @Positive(message = "Story ID must be a positive integer")

    @Column(name = "story_id")
    private int storyId;

    @Positive(message = "Maximum players must be a positive integer")
    @Column(name = "maximum_number_of_players_in_team")
    private int maximumPlayers;

    @Positive(message = "Admin ID must be a positive integer")
    @Column(name = "admin_id")
    private int adminId;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTeamModel> userTeamModelList;
}
