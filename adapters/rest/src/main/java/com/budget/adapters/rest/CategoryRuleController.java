package com.budget.adapters.rest;

import com.budget.core.CategoryRuleService;
import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryRuleEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/category-rule")
public class CategoryRuleController {
    @Autowired
    CategoryRuleService categoryRuleService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Returns the current category rules")
    @ApiResponse(responseCode = "200", description = "Category rules retrieved successfully")
    public ResponseEntity<List<CategoryRuleDto>> getCategoryRules() {
        return ResponseEntity.ok(categoryRuleService.getAllCategoryRules());
    }

    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Replace the category rules at runtime")
    @ApiResponse(responseCode = "200", description = "Category rules updated successfully")
    public ResponseEntity<Void> updateCategoryRules(@RequestBody List<CategoryRuleEntity> categoriesRules) {
        categoryRuleService.updateCategoryRules(categoriesRules);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Delete the category rules")
    @ApiResponse(responseCode = "200", description = "Category rules deleted successfully")
    public ResponseEntity<Void> deleteCategoryRules(@RequestBody List<UUID> ids) {
        categoryRuleService.deleteCategoryRules(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}