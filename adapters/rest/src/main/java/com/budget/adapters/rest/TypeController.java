package com.budget.adapters.rest;

import com.budget.core.TypeService;
import com.budget.core.entity.TypeEntity;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/type")
public class TypeController {
    @Autowired
    TypeService typeService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Returns a list of all types")
    @ApiResponse(responseCode = "200", description = "Type list retrieved successfully")
    public ResponseEntity<List<TypeEntity>> getTypeList() {
        return ResponseEntity.ok(typeService.getAllTypes());
    }
}