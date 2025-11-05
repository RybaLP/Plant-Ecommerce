package com.ecommerce.controller;

import com.ecommerce.dto.user.ClientContactInfoDto;
import com.ecommerce.dto.user.ClientProfileDto;
import com.ecommerce.dto.user.ClientResponseDto;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.user.ClientRepository;
import com.ecommerce.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @GetMapping("/me")
    public ResponseEntity<ClientProfileDto> getClientInfo () {
        return ResponseEntity.ok(clientService.getClientInfo());
    }

    @GetMapping("/contact")
    public ResponseEntity<ClientContactInfoDto> getClientPhoneNumberAndAddress () {
        return ResponseEntity.ok(clientService.getClientContactInfoDto());
    }

    @PostMapping("/contact")
    public ResponseEntity<ClientContactInfoDto> createClientContactDto (@Valid @RequestBody ClientContactInfoDto clientContactInfoDto){
        ClientContactInfoDto response = clientService.createClientContactInfo(clientContactInfoDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ClientResponseDto>> findAllClients () {
        List<ClientResponseDto> clientResponseDtoList = clientService.findAllClients();
        return ResponseEntity.ok(clientResponseDtoList);
    }


}