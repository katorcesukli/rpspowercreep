package org.personal.rpspowercreep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.personal.rpspowercreep.model.Game;
import org.personal.rpspowercreep.model.Player;
import org.personal.rpspowercreep.model.Round;
import org.personal.rpspowercreep.model.enums.ChoiceType;
import org.personal.rpspowercreep.model.enums.RoundResult;
import org.personal.rpspowercreep.repository.GameRepository;
import org.personal.rpspowercreep.repository.PlayerRepository;
import org.personal.rpspowercreep.repository.RoundRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;

    private final RoundPickerService roundPickerService;

    public Game createGame(String playerName) {

        Player player = new Player(playerName);
        playerRepository.save(player);

        Game game = new Game(player);

        return gameRepository.save(game);
    }

    public Game joinGame(String gameId, String playerName) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getPlayer2() != null) {
            throw new RuntimeException("Game already full");
        }

        Player player2 = new Player(playerName);
        playerRepository.save(player2);

        game.join(player2);

        return gameRepository.save(game);
    }

    @Transactional
    public Round submitMove(String gameId, String playerId, ChoiceType choice) {

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        Round round = getCurrentRound(game);

        if (player1.getPlayerId().equals(playerId)) {
            if (round.isPlayer1Submitted()) {
                throw new RuntimeException("Player1 already submitted this round");
            }
            round.setPlayer1Choice(choice);
            round.setPlayer1Submitted(true);

        } else if (player2.getPlayerId().equals(playerId)) {
            if (round.isPlayer2Submitted()) {
                throw new RuntimeException("Player2 already submitted this round");
            }
            round.setPlayer2Choice(choice);
            round.setPlayer2Submitted(true);

        } else {
            throw new RuntimeException("Player not in this game");
        }

        // Resolve only when both have submitted
        if (round.isPlayer1Submitted() && round.isPlayer2Submitted()) {

            RoundResult result = roundPickerService.resolve(
                    round.getPlayer1Choice(),
                    round.getPlayer2Choice()
            );

            round.setResult(result);
            roundRepository.save(round);

            updateScore(player1, player2, result);

            applyPowercreepToLoser(result, player1, player2);

            game.setCurrentRound(game.getCurrentRound() + 1);

            // Reset submission flags not needed — new round object will be created
            gameRepository.save(game);

            return round;
        }

        return null;
    }

    private void updateScore(Player p1, Player p2, RoundResult result) {

        switch (result) {
            case PLAYER1_WIN -> p1.setScore(p1.getScore() + 1);
            case PLAYER2_WIN -> p2.setScore(p2.getScore() + 1);
        }
    }

    private Round getCurrentRound(Game game) {
        return game.getRounds().stream()
                .filter(r -> r.getRoundNumber().equals(game.getCurrentRound()))
                .findFirst()
                .orElseGet(() -> {
                    Round newRound = new Round();
                    newRound.setRoundNumber(game.getCurrentRound());
                    newRound.setGame(game);
                    game.getRounds().add(newRound);
                    return newRound;
                });
    }

    private boolean checkGameFinished(Player p1, Player p2) {

        return p1.getScore() == 2 || p2.getScore() == 2;
    }

    private void applyPowercreepToLoser(RoundResult result, Player p1, Player p2) {
        Player loser = switch (result) {
            case PLAYER1_WIN -> p2;
            case PLAYER2_WIN -> p1;
            default -> null;
        };
        if (loser != null) {
            // Example: let the loser replace the first ability with a new one
            ChoiceType newAbility = ChoiceType.GUN; // could be dynamic
            ChoiceType replaced = loser.getAvailableChoices().get(0);
            loser.addChoice(newAbility, replaced);
            playerRepository.save(loser);
        }
    }

    public Game getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }
}
