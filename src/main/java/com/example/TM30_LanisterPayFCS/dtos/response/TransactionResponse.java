package com.example.TM30_LanisterPayFCS.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String appliedFeeId;
    private double appliedFeeValue;
    private double chargeAmount;
    private double settlementAmount;
}
