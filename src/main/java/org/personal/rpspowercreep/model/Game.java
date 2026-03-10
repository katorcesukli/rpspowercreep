package org.personal.rpspowercreep.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private String gameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_1_id")
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_2_id")
    private Player player2;

    private int currentRound;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();

    private boolean finished;

    public Game(Player player1) {
        this.gameId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.currentRound = 1;
    }

    public void join(Player player2) {
        this.player2 = player2;
    }

    public boolean isReady() {
        return player1 != null && player2 != null;
    }

    private Integer player1Score;
    private Integer player2Score;


}
