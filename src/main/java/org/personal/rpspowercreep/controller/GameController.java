package org.personal.rpspowercreep.controller;

import lombok.RequiredArgsConstructor;
import org.personal.rpspowercreep.model.Game;
import org.personal.rpspowercreep.model.Player;
import org.personal.rpspowercreep.model.Round;
import org.personal.rpspowercreep.model.dto.*;
import org.personal.rpspowercreep.model.enums.ChoiceType;
import org.personal.rpspowercreep.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public GameDTO createGame(@RequestParam String playerName) {
        Game game = gameService.createGame(playerName);
        return toGameDTO(game);
    }

    @PostMapping("/join")
    public GameDTO joinGame(@RequestParam String gameId, @RequestParam String playerName) {
        Game game = gameService.joinGame(gameId, playerName);
        return toGameDTO(game);
    }

    @PostMapping("/move")
    public MoveStatusDTO submitMove(@RequestParam String gameId,
                                    @RequestParam String playerId,
                                    @RequestParam ChoiceType choice) {

        Round round = gameService.submitMove(gameId, playerId, choice);

        Game game = gameService.getGameById(gameId); // get updated game state

        PlayerDTO p1 = toPlayerDTO(game.getPlayer1());
        PlayerDTO p2 = game.getPlayer2() != null ? toPlayerDTO(game.getPlayer2()) : null;

        if (round == null) {
            // only one player submitted, waiting for the other
            return new MoveStatusDTO(true, null, p1, p2);
        }

        // round resolved
        RoundDTO roundDTO = toRoundDTO(round);
        return new MoveStatusDTO(false, roundDTO, p1, p2);
    }

    @GetMapping("/{gameId}")
    public GameDTO getGame(@PathVariable String gameId) {
        Game game = gameService.getGameById(gameId);
        return toGameDTO(game);
    }

    // --- Mapping methods ---

    private GameDTO toGameDTO(Game game) {
        PlayerDTO p1 = toPlayerDTO(game.getPlayer1());
        PlayerDTO p2 = game.getPlayer2() != null ? toPlayerDTO(game.getPlayer2()) : null;
        List<RoundDTO> rounds = game.getRounds().stream()
                .map(this::toRoundDTO)
                .collect(Collectors.toList());

        return new GameDTO(game.getGameId(), p1, p2, game.getCurrentRound(), game.isFinished(), rounds);
    }

    private PlayerDTO toPlayerDTO(Player player) {
        return new PlayerDTO(
                player.getPlayerId(),
                player.getUsername(),
                player.getScore(),
                player.getAvailableChoices()
        );
    }

    private RoundDTO toRoundDTO(Round round) {
        return new RoundDTO(
                round.getRoundNumber(),
                round.getPlayer1Choice(),
                round.getPlayer2Choice(),
                round.getResult()
        );
    }
}
