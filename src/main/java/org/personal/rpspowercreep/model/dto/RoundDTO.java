package org.personal.rpspowercreep.model.dto;

import org.personal.rpspowercreep.model.enums.ChoiceType;
import org.personal.rpspowercreep.model.enums.RoundResult;

import java.util.List;

public record RoundDTO(

        Integer roundNumber,
        ChoiceType player1Choice,
        ChoiceType player2Choice,
        RoundResult result
) {


}
