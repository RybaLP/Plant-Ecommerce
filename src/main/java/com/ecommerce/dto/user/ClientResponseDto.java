package com.ecommerce.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClientResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime registrationDate;
    private AddressDto address;
}
