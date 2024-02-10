package com.example.TM30_LanisterPayFCS.service.implementation;

import com.example.TM30_LanisterPayFCS.data.models.Customer;
import com.example.TM30_LanisterPayFCS.data.models.Payment;
import com.example.TM30_LanisterPayFCS.dtos.request.ConfigRequest;
import com.example.TM30_LanisterPayFCS.dtos.request.TransactionRequest;
import com.example.TM30_LanisterPayFCS.dtos.response.ConfigResponse;
import com.example.TM30_LanisterPayFCS.dtos.response.TransactionResponse;
import com.example.TM30_LanisterPayFCS.exceptions.ConfigurationNotFoundException;
import com.example.TM30_LanisterPayFCS.exceptions.UnexpectedValueException;
import com.example.TM30_LanisterPayFCS.service.interfaces.ConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.OK;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeeConfigurationServiceTest {
    @Autowired
    private ConfigurationService configurationService;
    private ConfigResponse response;

    @BeforeEach
    void setUp() {
        ConfigRequest request = ConfigRequest.builder()
                .feeConfigurationSpec("LNPY1221 NGN * *(*) : APPLY PERC 1.4" +
                        "\nLNPY1222 NGN INTL CREDIT-CARD(VISA) : APPLY PERC 5.0" +
                        "\nLNPY1223 NGN LOCL CREDIT-CARD(*) : APPLY FLAT_PERC 50:1.4" +
                        "\nLNPY1224 NGN * BANK-ACCOUNT(*) : APPLY FLAT 100" +
                        "\nLNPY1225 NGN * USSD(MTN) : APPLY PERC 0.55")
                .build();

        response = configurationService.processFeeConfigurationSpec(request);
        assertEquals(OK, response.getStatus());
    }

    @Test
    void testThatFeeConfigSpecCanBeProcessed(){
        assertNotNull(response);
        assertEquals(OK,response.getStatus());
    }

    @Test
    void testThatErrorIsThrownIfFeeConfigSpecificationIsEmpty(){
        ConfigRequest request = ConfigRequest.builder().
                feeConfigurationSpec("")
                .build();

        assertThrows(UnexpectedValueException.class, ()-> configurationService.processFeeConfigurationSpec(request));
    }

    @Test
    void testToComputeTransactionFeeIfRequestIsValidAndTransactionMatchesAnyConfiguration(){
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .id("91203")
                .amount(BigDecimal.valueOf(5000))
                .currency("NGN")
                .currencyCountry("NG")
                .customer(new Customer(2211232L,"Ben Davies","ben@gmail.com",true))
                .paymentEntity(new Payment(2203454L,"GTBANK","MASTERCARD","530191******2903","CREDIT-CARD","NG","530191"))
                .build();

        TransactionResponse transactionResponse = configurationService.computeTransactionFee(transactionRequest);
        assertNotNull(transactionResponse);
        assertEquals(transactionResponse.getAppliedFeeValue(),120.0);
        assertEquals(transactionResponse.getChargeAmount(),5120.0);
        assertEquals(transactionResponse.getSettlementAmount(),5000.0);
    }

    @Test
    void testThatErrorIsThrownToComputeTransactionFeeIfTransactionDoesNotMatchAnyConfigurations(){
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .id("91205")
                .amount(BigDecimal.valueOf(3500))
                .currency("USD")
                .currencyCountry("US")
                .customer(new Customer(4211232L,"Wentworth Scoffield","wentworth@gmail.com",false))
                .paymentEntity(new Payment(2203454L,"WINTERFELLWALLETS","MASTERCARD","AX0923******02903","WALLET-ID","NG","AX0923"))
                .build();

        assertThrows(ConfigurationNotFoundException.class,()-> configurationService.computeTransactionFee(transactionRequest));
    }

//    @Test
//    void testThatErrorIsThrownToComputeTransactionFeeIfRequestFieldsNotValid(){
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .id("91203")
//                .amount(BigDecimal.valueOf(5000))
//                .currency("")
//                .currencyCountry("NG")
//                .customer(new Customer(2211232L,"Ben Davies","ben@gmail.com",true))
//                .paymentEntity(new Payment(2203454L,"GTBANK","MASTERCARD","530191******2903","CREDIT-CARD","NG","530191"))
//                .build();
//
//        assertThrows(UnexpectedValueException.class, ()-> configurationService.computeTransactionFee(transactionRequest));
//    }
//
//    @Test
//    void testThatErrorIsThrownToComputeTransactionFeeIfRequestFieldsForCustomerNotValid(){
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .id("91203")
//                .amount(BigDecimal.valueOf(5000))
//                .currency("")
//                .currencyCountry("NG")
//                .customer(new Customer(2211232L,"Ben Davies","",true))
//                .paymentEntity(new Payment(2203454L,"GTBANK","MASTERCARD","530191******2903","CREDIT-CARD","NG","530191"))
//                .build();
//
//        assertThrows(UnexpectedValueException.class, ()-> configurationService.computeTransactionFee(transactionRequest));
//    }
//
//    @Test
//    void testThatErrorIsThrownToComputeTransactionFeeIfRequestFieldsForPaymentEntityNotValid(){
//        TransactionRequest transactionRequest = TransactionRequest.builder()
//                .id("91203")
//                .amount(BigDecimal.valueOf(5000))
//                .currency("")
//                .currencyCountry("NG")
//                .customer(new Customer(2211232L,"Ben Davies","",true))
//                .paymentEntity(new Payment(null,"GTBANK","MASTERCARD","530191******2903","CREDIT-CARD","NG","530191"))
//                .build();
//
//        assertThrows(UnexpectedValueException.class, ()-> configurationService.computeTransactionFee(transactionRequest));
//    }
}