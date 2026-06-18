package com.budget.apis;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BudgetApi {
    public static String generateFileUrl = "/budget/budget/file/{bankName}";

    public static Response generateBankFileRaw(String bankName, File inputFile) {
        return given()
                .multiPart("file", inputFile)
                .contentType("multipart/form-data")
                .when()
                .post(generateFileUrl, bankName);
    }

    public static XSSFWorkbook generateBankFile(String bankName, File inputFile) throws IOException {
        Response response =
                given()
                        .multiPart("file", inputFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post(generateFileUrl, bankName)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    public static XSSFWorkbook generateAllBanksFile(List<String> bankNames, List<File> files) throws IOException {
        RequestSpecification request = given()
                .contentType("multipart/form-data");

        for (int i = 0; i < bankNames.size(); i++) {
            request.multiPart("bankNames", bankNames.get(i));

            request.multiPart(
                    "files",
                    files.get(i)              // <-- just File
            );
        }

        Response response = request
                .when()
                .post(generateFileUrl, "all")
                .then()
                .statusCode(200)
                .extract()
                .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger than 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }
}
