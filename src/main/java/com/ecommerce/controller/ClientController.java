package com.ecommerce.controller;

import com.ecommerce.dto.user.ClientContactInfoDto;
import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<ClientProfileDto> getClientInfo () {
        return ResponseEntity.ok(clientService.getClientInfo());
    }

    @GetMapping("/contact")
    public ResponseEntity<ClientContactInfoDto> getClientPhoneNumberAndAddress () {
        return ResponseEntity.ok(clientService.getClientContactInfoDto());
    }

}