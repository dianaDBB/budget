package com.budget.core.config;

import com.budget.core.repository.FileConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankConfigsConfiguration {

    private static final String TAB_DELIMITER = "\t";

    @Autowired
    private FileConfigRepository fileConfigRepository;

    @Bean
    public ActivoBankConfig activoBankConfig() {
        FileConfigEntity entity = fileConfigRepository.findByBankName("ActivoBank")
                .orElseThrow(() -> new IllegalStateException("ActivoBank config not found in database"));
        ActivoBankConfig config = new ActivoBankConfig();
        populate(config, entity);
        return config;
    }

    @Bean
    public CreditoAgricolaConfig creditoAgricolaConfig() {
        FileConfigEntity entity = fileConfigRepository.findByBankName("CreditoAgricola")
                .orElseThrow(() -> new IllegalStateException("CreditoAgricola config not found in database"));
        CreditoAgricolaConfig config = new CreditoAgricolaConfig();
        populate(config, entity);
        return config;
    }

    @Bean
    public CryptoComConfig cryptoComConfig() {
        FileConfigEntity entity = fileConfigRepository.findByBankName("CryptoCom")
                .orElseThrow(() -> new IllegalStateException("CryptoCom config not found in database"));
        CryptoComConfig config = new CryptoComConfig();
        populate(config, entity);
        return config;
    }

    private void populate(BankConfig config, FileConfigEntity entity) {
        config.setName(entity.getBankName());
        config.setFirstLine(entity.getFirstLine());
        config.setDateColumnPosition(entity.getDateColumnPos());
        config.setAmountColumnPosition(entity.getAmountColumnPos());
        config.setDescriptionColumnPosition(entity.getDescColumnPos());
        config.setDateFormat(entity.getDateformat());
        config.setDelimiter(entity.getDelimiter() != null ? entity.getDelimiter() : TAB_DELIMITER);
        config.setCdColumnPosition(entity.getCreditDebitColumnPos() != null ? entity.getCreditDebitColumnPos() : -1);
    }
}
