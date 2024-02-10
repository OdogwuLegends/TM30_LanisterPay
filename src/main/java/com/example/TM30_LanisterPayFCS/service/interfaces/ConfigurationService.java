package com.example.TM30_LanisterPayFCS.service.interfaces;

import com.example.TM30_LanisterPayFCS.dtos.request.ConfigRequest;
import com.example.TM30_LanisterPayFCS.dtos.request.TransactionRequest;
import com.example.TM30_LanisterPayFCS.dtos.response.ConfigResponse;
import com.example.TM30_LanisterPayFCS.dtos.response.TransactionResponse;

public interface ConfigurationService {
    ConfigResponse processFeeConfigurationSpec(ConfigRequest request);
    TransactionResponse computeTransactionFee(TransactionRequest request);
}
