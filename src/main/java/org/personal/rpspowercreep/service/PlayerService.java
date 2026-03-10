//handles login and registration
package org.personal.rpspowercreep.service;


import lombok.RequiredArgsConstructor;
import org.personal.rpspowercreep.model.Player;
import org.personal.rpspowercreep.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
    public Player register(String username, String password) throws Exception{

        String hashed
    }

     */
}
