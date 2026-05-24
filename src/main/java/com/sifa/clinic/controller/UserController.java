package com.sifa.clinic.controller;

import com.sifa.clinic.model.AppUser;
import com.sifa.clinic.model.Role;
import com.sifa.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Sadece DOCTOR rolündeki kullanıcıları getir
    @GetMapping("/doctors")
    public ResponseEntity<List<AppUser>> getDoctors() {
        List<AppUser> doctors = userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.DOCTOR)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }
}