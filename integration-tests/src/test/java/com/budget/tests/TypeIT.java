package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.TypeApi;
import com.budget.core.entity.TypeEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeIT extends BaseIT {
    @Test
    void shouldGetAll() {
        List<TypeEntity> types = TypeApi.getAll();
        assertEquals(4, types.size());
        assertEquals("Expense", types.get(0).getType());
        assertEquals("Income", types.get(1).getType());
        assertEquals("Savings/Investment", types.get(2).getType());
        assertEquals("TransferInHouse", types.get(3).getType());
    }
}
