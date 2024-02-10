package com.example.TM30_LanisterPayFCS.service.implementation;

import com.example.TM30_LanisterPayFCS.data.enums.FeeCurrency;
import com.example.TM30_LanisterPayFCS.data.enums.FeeEntity;
import com.example.TM30_LanisterPayFCS.data.enums.FeeLocale;
import com.example.TM30_LanisterPayFCS.data.enums.FeeType;
import com.example.TM30_LanisterPayFCS.data.models.FeeConfigEntry;
import com.example.TM30_LanisterPayFCS.data.repositories.FeeConfigEntryRepository;
import com.example.TM30_LanisterPayFCS.dtos.request.ConfigRequest;
import com.example.TM30_LanisterPayFCS.dtos.request.TransactionRequest;
import com.example.TM30_LanisterPayFCS.dtos.response.ConfigResponse;
import com.example.TM30_LanisterPayFCS.dtos.response.TransactionResponse;
import com.example.TM30_LanisterPayFCS.exceptions.ConfigurationNotFoundException;
import com.example.TM30_LanisterPayFCS.exceptions.UnexpectedValueException;
import com.example.TM30_LanisterPayFCS.service.interfaces.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.TM30_LanisterPayFCS.data.enums.FeeCurrency.NGN;
import static com.example.TM30_LanisterPayFCS.data.enums.FeeCurrency.STAR;
import static com.example.TM30_LanisterPayFCS.data.enums.FeeEntity.*;
import static com.example.TM30_LanisterPayFCS.data.enums.FeeLocale.INTL;
import static com.example.TM30_LanisterPayFCS.data.enums.FeeLocale.LOCL;
import static com.example.TM30_LanisterPayFCS.data.enums.FeeType.*;
import static com.example.TM30_LanisterPayFCS.utils.HardcodedValues.*;

@AllArgsConstructor
@Service
public class FeeConfigurationService implements ConfigurationService {
    private FeeConfigEntryRepository feeConfigEntryRepository;
    @Override
    public ConfigResponse processFeeConfigurationSpec(ConfigRequest request) {
        if(request.getFeeConfigurationSpec().isEmpty()){
            throw new UnexpectedValueException(EMPTY_SPECIFICATION);
        }

        String[] feeEntries = request.getFeeConfigurationSpec().split("\n");

        for (String feeEntry : feeEntries){
            FeeConfigEntry newConfigEntry = createFeeConfigEntry(feeEntry);
            feeConfigEntryRepository.save(newConfigEntry);
        }
        return new ConfigResponse(OK);
    }

    @Override
    public TransactionResponse computeTransactionFee(TransactionRequest request) {

//        if(isInvalidFields(request)){
//            throw new UnexpectedValueException(ALL_FIELDS_REQUIRED);
//        }

        FeeCurrency feeCurrency = getFeeCurrency(request);
        FeeLocale feeLocale = getLocale(request);
        FeeEntity feeEntity = getFeeEntityFromTransactionRequest(request);

        FeeConfigEntry feeConfigEntry = feeConfigEntryRepository.findByFeeCurrencyAndFeeLocaleAndFeeEntity(feeCurrency,feeLocale,feeEntity)
                .orElseThrow(() -> new ConfigurationNotFoundException(CONFIGURATION_NOT_FOUND));

        double appliedFeeValue = getAppliedFeeValue(request,feeConfigEntry);
        double chargeAmount = calculateChargeAmount(request,appliedFeeValue);
        double settlementAmount = chargeAmount - appliedFeeValue;


        return TransactionResponse.builder()
                .appliedFeeId(feeConfigEntry.getFeeId())
                .appliedFeeValue(Double.parseDouble(FORMAT_2F.formatted(appliedFeeValue)))
                .chargeAmount(chargeAmount)
                .settlementAmount(settlementAmount)
                .build();
    }

    private double calculateChargeAmount(TransactionRequest request, double appliedFeeValue) {
        double chargeAmount;
        if(request.getCustomer().isBearsFee()){
            chargeAmount = request.getAmount().doubleValue() + appliedFeeValue;
        } else {
            chargeAmount = request.getAmount().doubleValue();
        }
        return chargeAmount;
    }

    private double getAppliedFeeValue(TransactionRequest request, FeeConfigEntry feeConfigEntry) {
        return switch (feeConfigEntry.getFeeType()) {
            case FLAT -> Double.parseDouble(feeConfigEntry.getFeeValue());
            case PERC -> (Double.parseDouble(feeConfigEntry.getFeeValue()) / ONE_HUNDRED) * request.getAmount().doubleValue();
            case FLAT_PERC -> {
                String[] values = feeConfigEntry.getFeeValue().split(COLON);
                yield  Double.parseDouble(values[ZERO]) + (Double.parseDouble(values[ONE]) / ONE_HUNDRED) * request.getAmount().doubleValue();
            }
        };
    }

    private FeeLocale getLocale(TransactionRequest request){
        FeeEntity feeEntity = getFeeEntityFromTransactionRequest(request);
        if (!feeConfigEntryRepository.existsByFeeEntity(feeEntity)) {
            return FeeLocale.STAR;
        }

        if(request.getPaymentEntity().getType().equals(STRING_USSD)){
            if(feeConfigEntryRepository.existsByFeeEntityAndFeeLocale(USSD,request.getCurrencyCountry().equals(NG) ? LOCL : INTL)){
                return request.getCurrencyCountry().equals(NG) ? LOCL : INTL;
            }
            return FeeLocale.STAR;
        }
        return request.getCurrencyCountry().equals(NG) ? LOCL : INTL;
    }

    private FeeEntity getFeeEntityFromTransactionRequest(TransactionRequest request) {
        return switch (request.getPaymentEntity().getType()) {
            case STRING_CREDIT_CARD -> CREDIT_CARD;
            case STRING_DEBIT_CARD -> DEBIT_CARD;
            case STRING_BANK_ACCOUNT -> BANK_ACCOUNT;
            case STRING_USSD -> USSD;
            case STRING_WALLET_ID -> WALLET_ID;
            default -> FeeEntity.STAR;
        };
    }

    private FeeCurrency getFeeCurrency(TransactionRequest request) {
        if(request.getCurrency().equals(STRING_NGN)){
            return NGN;
        }
        return STAR;
    }

    private FeeConfigEntry createFeeConfigEntry(String feeEntry) {
        // "LNPY1222 NGN INTL CREDIT-CARD(VISA) : APPLY PERC 5.0"
        String[] parts = feeEntry.split(": APPLY ");

        // "LNPY1222 NGN INTL CREDIT-CARD(VISA)"
        String[] firstPartOfSplitString = parts[ZERO].split(SPACE);

        // LNPY1222
        String feeId = firstPartOfSplitString[ZERO];

        // NGN
        FeeCurrency feeCurrency;
        if(firstPartOfSplitString[ONE].equals(STRING_NGN)){
            feeCurrency = NGN;
        } else {
            feeCurrency = STAR;
        }

        // INTL
        FeeLocale feeLocale;
        if(firstPartOfSplitString[TWO].equals(STRING_LOCL)){
            feeLocale = LOCL;
        } else if (firstPartOfSplitString[TWO].equals(STRING_INTL)) {
            feeLocale = INTL;
        } else {
            feeLocale = FeeLocale.STAR;
        }

        // CREDIT-CARD(VISA)
        String feeEntityAndProperty = firstPartOfSplitString[THREE];

        // CREDIT-CARD
        FeeEntity feeEntity = getFeeEntity(feeEntityAndProperty);

        //VISA
        String feeEntityProperty = feeEntityAndProperty.substring(feeEntityAndProperty.indexOf("(")+ONE, feeEntityAndProperty.indexOf(")"));

        // PERC 5.0
        String secondPartOfSplitString = parts[ONE];

        // PERC
        String type = secondPartOfSplitString.split(SPACE)[ZERO];
        FeeType feeType = getFeeType(type);

        // 5.0
        String feeValue = secondPartOfSplitString.split(SPACE)[ONE];

        return new FeeConfigEntry(feeId,feeCurrency,feeLocale,feeEntity,feeType,feeEntityProperty,feeValue);
    }

    private FeeEntity getFeeEntity(String feeEntityAndProperty) {
        // CREDIT-CARD(VISA)
        String feeEntityString = feeEntityAndProperty.split("\\(")[ZERO];

        return switch (feeEntityString){
            case STRING_CREDIT_CARD -> CREDIT_CARD;
            case STRING_DEBIT_CARD -> DEBIT_CARD;
            case STRING_BANK_ACCOUNT -> BANK_ACCOUNT;
            case STRING_USSD -> USSD;
            case STRING_WALLET_ID -> WALLET_ID;
            case STRING_STAR -> FeeEntity.STAR;
            default -> throw new UnexpectedValueException(UNEXPECTED_VALUE + feeEntityString);
        };
    }

    private FeeType getFeeType(String feeType){
        return switch (feeType){
            case STRING_FLAT -> FLAT;
            case STRING_PERC -> PERC;
            case STRING_FLAT_PERC -> FLAT_PERC;
            default -> throw new UnexpectedValueException(UNEXPECTED_VALUE + feeType);
        };
    }

    private boolean isInvalidFields(TransactionRequest request){
        return request.getId().isEmpty() || request.getAmount() == null || request.getCurrency().isEmpty()
                || request.getCurrencyCountry().isEmpty() || request.getCustomer().getId() == null || request.getCustomer().getId().toString().isEmpty()
                || request.getCustomer().getFullName().isEmpty() || request.getCustomer().getEmailAddress().isEmpty()
                || (request.getCustomer().isBearsFee() != true && request.getCustomer().isBearsFee() != false)
                || request.getPaymentEntity().getId() == null || request.getPaymentEntity().getId().toString().isEmpty()
                || request.getPaymentEntity().getType().isEmpty() || request.getPaymentEntity().getBrand().isEmpty()
                || request.getPaymentEntity().getNumber().isEmpty() || request.getPaymentEntity().getIssuer().isEmpty()
                || request.getPaymentEntity().getCountry().isEmpty() || request.getPaymentEntity().getSixId().isEmpty();
    }
}
