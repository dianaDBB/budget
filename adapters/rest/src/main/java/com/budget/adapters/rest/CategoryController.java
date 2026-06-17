package com.budget.adapters.rest;

import com.budget.core.CategoryService;
import com.budget.core.config.CategoryEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Returns the current category rules")
    @ApiResponse(responseCode = "200", description = "Category rules retrieved successfully")
    public ResponseEntity<List<CategoryEntity>> getCategoryRules() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Replace the category rules at runtime")
    @ApiResponse(responseCode = "200", description = "Category rules updated successfully")
    public ResponseEntity<Void> updateCategoryRules(@RequestBody List<CategoryEntity> categories) {
        categoryService.updateCategories(categories);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}