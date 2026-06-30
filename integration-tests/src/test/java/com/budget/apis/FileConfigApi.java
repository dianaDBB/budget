package com.budget.apis;

import com.budget.core.dto.GetBankFileFormatResponseDto;
import com.budget.core.dto.UpdateFileConfigRequestDto;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class FileConfigApi {
    public static String updateConfigUrl = "/budget/file-config/{bankName}";

    public static Response updateConfig(String bankName, UpdateFileConfigRequestDto request) {
        return given().contentType("application/json").body(request).when().put(updateConfigUrl, bankName).then().extract().response();
    }

    public static GetBankFileFormatResponseDto getConfig(String bankName) {
        Response response = given().when().get(updateConfigUrl, bankName).then().statusCode(200).extract().response();

        return response.as(GetBankFileFormatResponseDto.class);
    }
}
