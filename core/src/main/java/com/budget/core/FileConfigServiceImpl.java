package com.budget.core;

import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.core.entity.FileConfigEntity;
import com.budget.core.repository.FileConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileConfigServiceImpl implements FileConfigService {

    @Autowired
    FileConfigRepository fileConfigRepository;

    @Override
    public FileConfigEntity getBankFileConfig(String bankName) {
        return fileConfigRepository.findByBankName(bankName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown bank: " + bankName));
    }

    @Override
    public List<FileConfigEntity> getAllBankFileFormats() {
        return fileConfigRepository.findAll();
    }

    @Override
    public void updateBankFileConfig(String bankName, UpdateFileConfigRequestDto request) {
        FileConfigEntity entity = fileConfigRepository.findByBankName(bankName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown bank: " + bankName));

        if (request.getFirstLine() != null) entity.setFirstLine(request.getFirstLine());
        if (request.getDateColumnPosition() != null) entity.setDateColumnPos(request.getDateColumnPosition());
        if (request.getAmountColumnPosition() != null) entity.setAmountColumnPos(request.getAmountColumnPosition());
        if (request.getDescColumnPosition() != null) entity.setDescColumnPos(request.getDescColumnPosition());
        if (request.getCdColumnPosition() != null) entity.setDescColumnPos(request.getCdColumnPosition());
        if (request.getDateFormat() != null) entity.setDateFormat(request.getDateFormat());
        if (request.getDelimiter() != null) entity.setDelimiter(request.getDelimiter());

        fileConfigRepository.save(entity);
    }
}
