//handles login and registration
package org.personal.rpspowercreep.service;


import lombok.RequiredArgsConstructor;
import org.personal.rpspowercreep.model.Player;
import org.personal.rpspowercreep.model.dto.auth.LoginRequest;
import org.personal.rpspowercreep.model.dto.auth.RegisterRequest;
import org.personal.rpspowercreep.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Player register(String username, String password) throws Exception{
        if (playerRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Player player = Player.builder()
                .username(username)
                .password(hashedPassword).build();

        return playerRepository.save(player);
    }

    public Player login(String username, String rawPassword) throws Exception {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Invalid username or password"));

        if (!passwordEncoder.matches(rawPassword, player.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        return player;
    }

    public Player register(RegisterRequest request) throws Exception {
        return register(request.username(), request.password());
    }

    public Player login(LoginRequest request) throws Exception {
        return login(request.username(), request.password());
    }


}
