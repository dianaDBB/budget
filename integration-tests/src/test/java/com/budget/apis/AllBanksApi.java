package com.budget.apis;

import io.restassured.response.Response;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllBanksApi {
    public static String url = "/budget/budget/file/all";

    public static XSSFWorkbook uploadAllBankFiles(File activoBankFile, File creditoAgricolaFile, File cryptoComFile) throws IOException {
        Response response =
                given()
                        .multiPart("activoBankFile", activoBankFile)
                        .multiPart("creditoAgricolaFile", creditoAgricolaFile)
                        .multiPart("cryptoComFile", cryptoComFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post(url)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger than 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }
}

