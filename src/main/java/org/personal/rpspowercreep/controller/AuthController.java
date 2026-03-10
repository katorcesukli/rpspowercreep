package org.personal.rpspowercreep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.personal.rpspowercreep.model.Player;
import org.personal.rpspowercreep.model.dto.auth.LoginRequest;
import org.personal.rpspowercreep.model.dto.auth.PlayerRequest;
import org.personal.rpspowercreep.model.dto.auth.RegisterRequest;
import org.personal.rpspowercreep.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerService playerService;

    @PostMapping("/register")
    public Player register(@RequestBody @Valid RegisterRequest request) throws Exception {
        return playerService.register(request.username());
    }

    @PostMapping("/login")
    public Player login(@RequestBody @Valid LoginRequest request) throws Exception {
        return playerService.login(request.username());
    }

    @PostMapping("/join")
    public Player join(@RequestBody @Valid PlayerRequest request) {
        return playerService.loginOrRegister(request.username());
    }
}
