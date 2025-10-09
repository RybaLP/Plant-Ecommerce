package com.ecommerce.controller;

import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<ClientProfileDto> getClientInfo () {
        return ResponseEntity.ok(clientService.getClientInfo());
    }

}
