package com.budget.adapters.rest;

import com.budget.core.CategoryService;
import com.budget.core.entity.CategoryEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Returns a list of all categories")
    @ApiResponse(responseCode = "200", description = "Category list retrieved successfully")
    public ResponseEntity<List<CategoryEntity>> getCategoryList() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}