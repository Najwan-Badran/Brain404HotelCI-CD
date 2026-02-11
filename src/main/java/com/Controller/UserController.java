package com.Controller;

import com.DTO.*;
import com.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.findAll();
    }
}
