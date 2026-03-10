package org.personal.rpspowercreep.service;

import org.personal.rpspowercreep.model.enums.ChoiceType;
import org.personal.rpspowercreep.model.enums.RoundResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RoundPickerService {

    private final Map<ChoiceType, Set<ChoiceType>> rules = new HashMap<>();

    public RoundPickerService() {
        initRules();
    }

    private void initRules() {
        //match up chart
        rules.put(ChoiceType.ROCK, Set.of(ChoiceType.SCISSORS));
        rules.put(ChoiceType.PAPER, Set.of(ChoiceType.ROCK));
        rules.put(ChoiceType.SCISSORS, Set.of(ChoiceType.PAPER));

        //powercreep upgrade set 1
        rules.put(ChoiceType.GUN, Set.of(ChoiceType.PAPER, ChoiceType.ROCK, ChoiceType.KNIFE));
        rules.put(ChoiceType.KNIFE, Set.of(ChoiceType.SCISSORS, ChoiceType.PAPER, ChoiceType.ARNIS));
        rules.put(ChoiceType.ARNIS, Set.of(ChoiceType.SCISSORS, ChoiceType.ROCK, ChoiceType.GUN));
    }

    public RoundResult resolve(ChoiceType p1, ChoiceType p2) {

        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Pick one coward");
        }

        if (p1 == p2) {
            return RoundResult.DRAW;
        }

        if (rules.getOrDefault(p1, Set.of()).contains(p2)) {
            return RoundResult.PLAYER1_WIN;
        }

        if (rules.getOrDefault(p2, Set.of()).contains(p1)) {
            return RoundResult.PLAYER2_WIN;
        }

        throw new IllegalStateException("No rule defined for " + p1 + " vs " + p2);
    }
}
