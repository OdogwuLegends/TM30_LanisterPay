package com.example.TM30_LanisterPayFCS.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.VALUE_NOT_BLANK;
import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.VALUE_NOT_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigRequest {

    @NotBlank(message = VALUE_NOT_BLANK)
    @NotNull(message = VALUE_NOT_NULL)
    private String feeConfigurationSpec;
}
