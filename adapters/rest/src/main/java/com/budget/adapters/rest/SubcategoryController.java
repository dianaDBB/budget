package com.budget.adapters.rest;

import com.budget.core.SubcategoryService;
import com.budget.core.entity.SubcategoryEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/subcategory")
public class SubcategoryController {
    @Autowired
    SubcategoryService subcategoryService;

    @GetMapping(value = "/{categoryName}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Returns a list of all subcategories for a category")
    @ApiResponse(responseCode = "200", description = "Category list retrieved successfully")
    public ResponseEntity<List<SubcategoryEntity>> getCategoryList(@PathVariable String categoryName) {
        return ResponseEntity.ok(subcategoryService.getAllSubcategories(categoryName));
    }
}