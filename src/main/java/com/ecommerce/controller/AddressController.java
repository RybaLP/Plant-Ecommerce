package com.ecommerce.controller;

import com.ecommerce.dto.user.AddressDto;
import com.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @PatchMapping
    public ResponseEntity<AddressDto> updateUserAddress (@RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(addressDto));
    }
}
