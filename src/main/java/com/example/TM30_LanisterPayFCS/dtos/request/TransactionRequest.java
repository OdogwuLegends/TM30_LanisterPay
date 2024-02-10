package com.example.TM30_LanisterPayFCS.dtos.request;

import com.example.TM30_LanisterPayFCS.data.models.Customer;
import com.example.TM30_LanisterPayFCS.data.models.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.VALUE_NOT_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @NotNull(message = VALUE_NOT_NULL)
    private String id;

    @NotNull(message = VALUE_NOT_NULL)
    private BigDecimal amount;

    @NotNull(message = VALUE_NOT_NULL)
    private String currency;

    @NotNull(message = VALUE_NOT_NULL)
    private String currencyCountry;

    @NotNull(message = VALUE_NOT_NULL)
    private Customer customer;

    @NotNull(message = VALUE_NOT_NULL)
    private Payment paymentEntity;
}
