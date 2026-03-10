package org.personal.rpspowercreep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personal.rpspowercreep.model.enums.ChoiceType;
import org.personal.rpspowercreep.model.enums.RoundResult;

@Entity
@Table(name = "round")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roundId;

    private Integer roundNumber;

    @Enumerated(EnumType.STRING)
    private ChoiceType player1Choice;

    @Enumerated(EnumType.STRING)
    private ChoiceType player2Choice;

    @Enumerated(EnumType.STRING)
    private RoundResult result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private boolean player1Submitted = false;
    private boolean player2Submitted = false;
}
