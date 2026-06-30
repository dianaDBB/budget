package com.budget.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.budget.dto.EntryDto;

public class CryptoCom {
    public static File createValidFile(List<EntryDto> entryList) throws IOException {
        File file = new File("target/test-cryptoCom-" + System.currentTimeMillis() + ".csv");
        StringBuilder content = new StringBuilder("Timestamp (UTC),Transaction Description,Currency,Amount,To " +
                "Currency,To Amount,Native Currency,Native Amount,Native Amount (in USD),Transaction Kind," +
                "Transaction" + " Hash\n");

        for (EntryDto entry : entryList) {
            double nativeAmount = entry.amount() * 1.1;
            content.append(String.format("2026-06-09 10:00:00,%s,EUR,%.2f,,,EUR,%.2f,%.2f,,\n", entry.description(),
                    entry.amount(), entry.amount(), nativeAmount));
        }

        Files.write(file.toPath(), content.toString().getBytes());
        return file;
    }

    public static File createInvalidFile(String invalidValue) throws IOException {
        File file = new File("target/test-cryptoCom-" + System.currentTimeMillis() + ".csv");

        Files.write(file.toPath(),
                ("Timestamp (UTC),Transaction Description,Currency,Amount,To Currency,To Amount," + "Native Currency," +
                        "Native Amount,Native Amount (in USD),Transaction Kind,Transaction Hash\n" + String.format(
                                "2026-06-09 10:00:00,Invalid value,EUR,%s,,,EUR,8,8,,\n", invalidValue)).getBytes());
        return file;
    }
}
