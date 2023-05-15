package ru.salychev.expensiveprofit.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void testSetAndGetUnitId() {
        Unit unit = new Unit();
        unit.setId(1L);
        assertEquals(1L, unit.getId());
    }

    @Test
    void testSetAndGetUnitName() {
        Unit unit = new Unit();
        unit.setName("testUnit3");
        assertEquals("testUnit3", unit.getName());
    }

    @Test
    void testUnitConstructor() {
        Unit unit = new Unit("testUnit2");
        Long testId = unit.getId();
        assertEquals(testId, unit.getId());
        assertEquals("testUnit2", unit.getName());
    }

    @Test
    void testToStringUnit() {
        Unit unit = new Unit("testUnit1");
        unit.setId(1234567890123L);
        String expected = "Unit{id=1234567890123, name='testUnit1'}";
        assertEquals(expected, unit.toString());
    }
}