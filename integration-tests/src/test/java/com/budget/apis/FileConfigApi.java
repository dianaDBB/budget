package com.budget.apis;

import com.budget.adapters.rest.dto.BankFileFormatDto;
import com.budget.core.config.BankConfigRequest;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class FileConfigApi {
    public static String updateConfigUrl = "/budget/file-config/{bankName}";

    public static Response updateConfig(String bankName, BankConfigRequest request) {
        return given()
                .contentType("application/json")
                .body(request)
                .when()
                .put(updateConfigUrl, bankName)
                .then()
                .extract()
                .response();
    }

    public static BankFileFormatDto getConfig(String bankName) {
        Response response =
                given()
                        .when()
                        .get(updateConfigUrl, bankName)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        return response.as(BankFileFormatDto.class);
    }
}
