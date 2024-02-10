package com.example.TM30_LanisterPayFCS.controller;

import com.example.TM30_LanisterPayFCS.dtos.request.ConfigRequest;
import com.example.TM30_LanisterPayFCS.dtos.request.TransactionRequest;
import com.example.TM30_LanisterPayFCS.dtos.response.ConfigResponse;
import com.example.TM30_LanisterPayFCS.dtos.response.TransactionResponse;
import com.example.TM30_LanisterPayFCS.service.interfaces.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lanister")
@AllArgsConstructor
public class FeeConfigurationController {

    private ConfigurationService configurationService;
    @PostMapping("/fees")
    public ResponseEntity<ConfigResponse> processFeeConfigurationSpec(@RequestBody ConfigRequest request){
        ConfigResponse response = configurationService.processFeeConfigurationSpec(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/compute-transaction-fee")
    public ResponseEntity<TransactionResponse> computeTransactionFee(@RequestBody TransactionRequest request){
        TransactionResponse response = configurationService.computeTransactionFee(request);
        return ResponseEntity.ok().body(response);
    }
}
