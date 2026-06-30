package com.budget.apis;

import com.budget.core.entity.CategoryEntity;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CategoryApi {
    public static String getUrl = "/budget/category/all";

    public static List<CategoryEntity> getAll() {
        Response response = given().when().get(getUrl).then().statusCode(200).extract().response();

        return response.as(new TypeRef<>() {
        });
    }
}
