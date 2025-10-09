package com.ecommerce.dto.user;

import com.ecommerce.model.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

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
