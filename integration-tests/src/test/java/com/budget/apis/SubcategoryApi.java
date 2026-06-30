package com.budget.apis;

import com.budget.core.entity.SubcategoryEntity;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SubcategoryApi {
    public static String getUrl = "/budget/subcategory/{categoryName}/all";

    public static List<SubcategoryEntity> getAll(String categoryName) {
        Response response = given().when().get(getUrl, categoryName).then().statusCode(200).extract().response();

        return response.as(new TypeRef<>() {
        });
    }
}
