package com.example.TM30_LanisterPayFCS.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.VALUE_NOT_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull(message = VALUE_NOT_NULL)
    private String issuer;

    @NotNull(message = VALUE_NOT_NULL)
    private String brand;

    @NotNull(message = VALUE_NOT_NULL)
    private String number;

    @NotNull(message = VALUE_NOT_NULL)
    private String type;

    @NotNull(message = VALUE_NOT_NULL)
    private String country;

    @NotNull(message = VALUE_NOT_NULL)
    private String sixId;
}
