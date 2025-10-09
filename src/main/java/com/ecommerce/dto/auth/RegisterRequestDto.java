package com.ecommerce.dto.auth;

import com.ecommerce.dto.user.AddressDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank @Size(min = 3, max = 20)
    private String username;

    @NotBlank @Size(min = 6)
    private String password;

    @NotBlank @Email
    private String email;

    @Size(max = 50)
    private String firstName ;

    @Size(max = 50)
    private String lastName ;

    @Pattern(regexp = "\\d{9}", message = "Phone number should have 9 digits")
    private String phoneNumber;

    private AddressDto shippingAddress;
}
