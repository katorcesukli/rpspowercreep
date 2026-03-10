package org.personal.rpspowercreep.model;

import io.netty.handler.codec.socks.SocksAuthRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personal.rpspowercreep.model.enums.ChoiceType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String playerId;

    @Column(unique = true, nullable = false)
    private String username;


    private Integer score = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "player_choices", joinColumns = @JoinColumn(name = "player_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "choice")
    private List<ChoiceType> availableChoices = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChoiceType currentChoice;

    public Player(String name) {
        this.score = 0;
        this.username = name;

        availableChoices.add(ChoiceType.ROCK);
        availableChoices.add(ChoiceType.PAPER);
        availableChoices.add(ChoiceType.SCISSORS);
    }

    public void addChoice(ChoiceType newChoice, ChoiceType replacedChoice) {
        availableChoices.remove(replacedChoice);
        availableChoices.add(newChoice);
    }
}
