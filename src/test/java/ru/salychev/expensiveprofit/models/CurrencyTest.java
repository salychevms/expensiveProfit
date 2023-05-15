package ru.salychev.expensiveprofit.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testSetAndGetCurrencyId() {
        Currency currency = new Currency();
        currency.setId(1L);
        assertEquals(1L, currency.getId());
    }

    @Test
    void testSetAndGetCurrencyName() {
        Currency currency = new Currency();
        currency.setName("testCurrency3");
        assertEquals("testCurrency3", currency.getName());
    }

    @Test
    void testCurrencyConstructor() {
        Currency currency = new Currency("testCurrency2");
        Long testId = currency.getId();
        assertEquals(testId, currency.getId());
        assertEquals("testCurrency2", currency.getName());
    }

    @Test
    void testToStringCurrency() {
        Currency currency = new Currency("testCurrency1");
        currency.setId(1234567890123L);
        String expected = "Currency{id=1234567890123, name='testCurrency1'}";
        assertEquals(expected, currency.toString());
    }
}