package com.budget.core;

import com.budget.core.config.FileConfigEntity;
import com.budget.core.config.BankConfigRequest;
import com.budget.core.repository.FileConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileConfigServiceImpl implements FileConfigService {

    @Autowired
    FileConfigRepository fileConfigRepository;

    @Override
    public FileConfigEntity getBankFileConfig(String bankName) {
        return fileConfigRepository.findByBankName(resolveName(bankName))
                .orElseThrow(() -> new IllegalArgumentException("Unknown bank: " + bankName));
    }

    @Override
    public void updateBankFileConfig(String bankName, BankConfigRequest request) {
        fileConfigRepository.findByBankName(resolveName(bankName)).ifPresent(entity -> {
            if (request.getFirstLine() != null) entity.setFirstLine(request.getFirstLine());
            if (request.getDelimiter() != null) entity.setDelimiter(request.getDelimiter());
            if (request.getDateFormat() != null) entity.setDateformat(request.getDateFormat());
            if (request.getAmountColumnPosition() != null) entity.setAmountColumnPos(request.getAmountColumnPosition());
            if (request.getDateColumnPosition() != null) entity.setDateColumnPos(request.getDateColumnPosition());
            if (request.getDescriptionColumnPosition() != null)
                entity.setDescColumnPos(request.getDescriptionColumnPosition());
            if (request.getCdColumnPosition() != null)
                entity.setCreditDebitColumnPos(request.getCdColumnPosition() == -1 ? null : request.getCdColumnPosition());
            fileConfigRepository.save(entity);
        });
    }

    private String resolveName(String bankName) {
        return switch (bankName.toLowerCase()) {
            case "activobank" -> "ActivoBank";
            case "creditoagricola" -> "CreditoAgricola";
            case "cryptocom" -> "CryptoCom";
            default -> throw new IllegalArgumentException("Unknown bank: " + bankName);
        };
    }
}
