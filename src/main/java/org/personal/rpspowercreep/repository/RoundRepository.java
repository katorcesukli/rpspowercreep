package org.personal.rpspowercreep.repository;

import org.personal.rpspowercreep.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, String> {

    List<Round> findByRoundId(String gameId);
}
