package com.sifa.clinic.controller;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.repository.UserRepository;
import com.sifa.clinic.security.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten alınmış!");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Şifreyi açık kaydetmek yasak!
        user.setRole(request.getRole());
        
        userRepository.save(user);

        // Kullanıcıya hemen token ver
        String jwtToken = jwtService.generateToken(
                User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole().name()).build()
        );
        
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Spring Security arka planda şifreleri karşılaştırır, yanlışsa hata fırlatır
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AppUser user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(
                User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole().name()).build()
        );
        
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}

// ---- DTO Sınıfları (Veri Taşıyıcılar) ----
@Data
class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class AuthResponse {
    private final String token;
}