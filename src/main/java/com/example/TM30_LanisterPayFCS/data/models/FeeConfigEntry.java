package com.example.TM30_LanisterPayFCS.data.models;

import com.example.TM30_LanisterPayFCS.data.enums.FeeCurrency;
import com.example.TM30_LanisterPayFCS.data.enums.FeeEntity;
import com.example.TM30_LanisterPayFCS.data.enums.FeeLocale;
import com.example.TM30_LanisterPayFCS.data.enums.FeeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.FEE_CONFIGURATION;
import static jakarta.persistence.EnumType.STRING;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = FEE_CONFIGURATION)
public class FeeConfigEntry {
    @Id
    private String feeId;

    @Enumerated(STRING)
    private FeeCurrency feeCurrency;

    @Enumerated(STRING)
    private FeeLocale feeLocale;

    @Enumerated(STRING)
    private FeeEntity feeEntity;

    @Enumerated(STRING)
    private FeeType feeType;

    private String feeEntityProperty;

    private String feeValue;
}
