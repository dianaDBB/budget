package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.CategoryApi;
import com.budget.core.entity.CategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryIT extends BaseIT {
    @Test
    void shouldGetAll() {
        List<CategoryEntity> categories = CategoryApi.getAll();
        assertEquals(10, categories.size());
        assertEquals("Home", categories.get(0).getCategory());
        assertEquals("Utilities", categories.get(1).getCategory());
        assertEquals("Daily_Livings", categories.get(2).getCategory());
        assertEquals("Cars", categories.get(3).getCategory());
        assertEquals("Vacation", categories.get(4).getCategory());
        assertEquals("Bank", categories.get(5).getCategory());
        assertEquals("Job´s", categories.get(6).getCategory());
        assertEquals("Pets", categories.get(7).getCategory());
        assertEquals("Business", categories.get(8).getCategory());
        assertEquals("House_Constructions", categories.get(9).getCategory());
    }
}
