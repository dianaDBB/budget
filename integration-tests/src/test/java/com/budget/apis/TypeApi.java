package com.budget.apis;

import com.budget.core.entity.TypeEntity;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TypeApi {
    public static String getUrl = "/budget/type/all";

    public static List<TypeEntity> getAll() {
        Response response = given().when().get(getUrl).then().statusCode(200).extract().response();

        return response.as(new TypeRef<>() {
        });
    }
}
