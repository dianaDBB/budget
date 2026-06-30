package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.SubcategoryApi;
import com.budget.core.entity.SubcategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubcategoryIT extends BaseIT {
    @Test
    void shouldGetAllFromCategory() {
        List<SubcategoryEntity> subcategories = SubcategoryApi.getAll("Home");
        assertEquals(10, subcategories.size());
        assertEquals("Groceries", subcategories.get(0).getSubcategory());
        assertEquals("Butcher", subcategories.get(1).getSubcategory());
        assertEquals("Loan", subcategories.get(2).getSubcategory());
        assertEquals("Furnishing/Appliances", subcategories.get(3).getSubcategory());
        assertEquals("Garden", subcategories.get(4).getSubcategory());
        assertEquals("Pool", subcategories.get(5).getSubcategory());
        assertEquals("Insurances", subcategories.get(6).getSubcategory());
        assertEquals("IMI", subcategories.get(7).getSubcategory());
        assertEquals("Maintenance/Improvements", subcategories.get(8).getSubcategory());
        assertEquals("Smart Home", subcategories.get(9).getSubcategory());
    }

    @Test
    void shouldReturnEmptyListWhenCategoryNotFound() {
        List<SubcategoryEntity> subcategories = SubcategoryApi.getAll("NOT_FOUND");
        assertEquals(0, subcategories.size());
    }
}
