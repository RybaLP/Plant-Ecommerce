package com.ecommerce.dto.user;

import com.ecommerce.dto.cart.CartDto;
import com.ecommerce.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private CartDto cart;
}
