package com.maturidade.ti.controller;

import com.maturidade.ti.dto.LoginRequestDTO;
import com.maturidade.ti.dto.LoginResponseDTO;
import com.maturidade.ti.dto.UsuarioRequestDTO;
import com.maturidade.ti.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/login") public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) { return authService.login(dto); }
    @PostMapping("/register") public LoginResponseDTO register(@RequestBody UsuarioRequestDTO dto) { return authService.register(dto); }
}
