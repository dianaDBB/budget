package com.budget.apis;

import com.budget.adapters.rest.BankFileFormatDto;
import io.restassured.response.Response;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CryptoComApi {
    public static String generateFileUrl = "/budget/budget/file/cryptoCom";
    public static String getFormatUrl = "/budget/budget/format/cryptoCom";

    public static File createValidFile(List<EntryDto> entryList) throws IOException {
        File file = new File("target/test-cryptoCom-" + System.currentTimeMillis() + ".csv");
        StringBuilder content = new StringBuilder("Timestamp (UTC),Transaction Description,Currency,Amount,To Currency,To Amount,Native Currency,Native Amount,Native Amount (in USD),Transaction Kind,Transaction Hash\n");

        for (EntryDto entry : entryList) {
            double nativeAmount = entry.amount() * 1.1;
            content.append(String.format("2026-06-09 10:00:00,%s,EUR,%.2f,,,EUR,%.2f,%.2f,,\n", entry.description(), entry.amount(), entry.amount(), nativeAmount));
        }

        Files.write(file.toPath(), content.toString().getBytes());
        return file;
    }

    public static File createInvalidFile(String invalidValue) throws IOException {
        File file = new File("target/test-cryptoCom-" + System.currentTimeMillis() + ".csv");

        Files.write(file.toPath(), ("Timestamp (UTC),Transaction Description,Currency,Amount,To Currency,To Amount,Native Currency,Native Amount,Native Amount (in USD),Transaction Kind,Transaction Hash\n"
                + String.format("2026-06-09 10:00:00,Invalid value,EUR,%s,,,EUR,8,8,,\n", invalidValue)).getBytes());
        return file;
    }

    public static XSSFWorkbook generateFile(File inputFile) throws IOException {
        Response response = given().multiPart("file", inputFile).contentType("multipart/form-data").when().post(CryptoComApi.generateFileUrl).then().statusCode(200).extract().response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    public static BankFileFormatDto getFormat() {
        Response response =
                given()
                        .when()
                        .get(getFormatUrl)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        return response.as(BankFileFormatDto.class);
    }
}
