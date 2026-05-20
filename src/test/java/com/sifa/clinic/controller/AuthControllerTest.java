package com.sifa.clinic.controller;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.repository.UserRepository;
import com.sifa.clinic.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ShouldReturnToken_WhenUserIsNew() {
        // Hazırlık
        RegisterRequest request = new RegisterRequest();
        request.setUsername("doktor_ali");
        request.setPassword("12345");
        request.setRole(Role.DOCTOR);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        // Aksiyon
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Doğrulama
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("fake-jwt-token", response.getBody().getToken());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        // Hazırlık
        LoginRequest request = new LoginRequest();
        request.setUsername("doktor_ali");
        request.setPassword("12345");

        AppUser mockUser = new AppUser(1L, "doktor_ali", "hashed_password", Role.DOCTOR);
        
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        // Aksiyon
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Doğrulama
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fake-jwt-token", response.getBody().getToken());
    }
}