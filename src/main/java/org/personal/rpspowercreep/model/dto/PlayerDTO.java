package org.personal.rpspowercreep.model.dto;

import org.personal.rpspowercreep.model.enums.ChoiceType;

import java.util.List;

public record PlayerDTO(

        String id,
        String username,
        Integer score,
        List<ChoiceType> availableChoices
) {
}
