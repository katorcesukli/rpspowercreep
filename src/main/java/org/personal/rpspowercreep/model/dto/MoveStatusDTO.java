package org.personal.rpspowercreep.model.dto;

public record MoveStatusDTO(

        boolean waiting,
        RoundDTO round,

        PlayerDTO player1,
        PlayerDTO player2

) {
}
