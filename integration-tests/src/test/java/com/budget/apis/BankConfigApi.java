package com.budget.apis;

import com.budget.core.config.BankConfigRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BankConfigApi {
    public static String updateConfigUrl = "/budget/budget/config/{bankName}";

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
}
