package ru.salychev.expensiveprofit.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {
    @Test
    void testSetAndGetTypeId() {
        Type type = new Type();
        type.setId(1L);
        assertEquals(1L, type.getId());
    }

    @Test
    void testSetAndGetTypeName() {
        Type type=new Type();
        type.setName("testType3");
        assertEquals("testType3", type.getName());
    }

    @Test
    void testTypeConstructor() {
        Type type = new Type("testType2");
        Long testId = type.getId();
        assertEquals(testId, type.getId());
        assertEquals("testType2", type.getName());
    }

    @Test
    void testToStringType() {
        Type type = new Type("testType1");
        type.setId(1234567890123L);
        String expected = "Type{id=1234567890123, name='testType1'}";
        assertEquals(expected, type.toString());
    }
}