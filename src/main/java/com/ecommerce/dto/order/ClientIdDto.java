package com.ecommerce.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientIdDto {

    @Min(value = 1, message = "clientId must be greated than 0")
    @NotNull(message = "Client id must be provided")
    private Long clientId;
}
