package org.personal.rpspowercreep.model.dto;

import org.personal.rpspowercreep.model.enums.ChoiceType;

public record ReplaceChoiceRequestDTO(

        String gameId,
        String playerId,
        ChoiceType newChoice,
        ChoiceType oldChoice


) {
}
