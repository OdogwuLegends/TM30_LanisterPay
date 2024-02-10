package com.example.TM30_LanisterPayFCS.data.repositories;

import com.example.TM30_LanisterPayFCS.data.enums.FeeCurrency;
import com.example.TM30_LanisterPayFCS.data.enums.FeeEntity;
import com.example.TM30_LanisterPayFCS.data.enums.FeeLocale;
import com.example.TM30_LanisterPayFCS.data.models.FeeConfigEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeConfigEntryRepository extends JpaRepository<FeeConfigEntry,String> {
    Optional<FeeConfigEntry> findByFeeCurrencyAndFeeLocaleAndFeeEntity(FeeCurrency feeCurrency, FeeLocale feeLocale, FeeEntity feeEntity);
    boolean existsByFeeEntityAndFeeLocale(FeeEntity feeEntity, FeeLocale feeLocale);
    boolean existsByFeeEntity(FeeEntity feeEntity);
}
