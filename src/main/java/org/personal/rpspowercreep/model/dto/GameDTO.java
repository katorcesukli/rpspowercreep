package org.personal.rpspowercreep.model.dto;

import java.util.List;

public record GameDTO(

        String id,
        PlayerDTO player1,
        PlayerDTO player2,
        Integer currentRound,
        boolean finished,
        List<RoundDTO> rounds
) {
}
