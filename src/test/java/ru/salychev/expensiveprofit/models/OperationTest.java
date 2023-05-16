package ru.salychev.expensiveprofit.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void testSetAndGetOperation() {
        Operation operation = new Operation();
        operation.setOperation("testOperation");
        assertEquals("testOperation", operation.getOperation());
    }

    @Test
    void testSetAndGetId() {
        Operation operation = new Operation();
        operation.setId(1234567890123L);
        assertEquals(1234567890123L, operation.getId());
    }

    @Test
    void testSetAndGetUser() {
        User testUser = new User();
        Operation operation = new Operation();
        operation.setUser(testUser);
        assertEquals(testUser, operation.getUser());
    }

    @Test
    void testSetAndGetType() {
        Type testType = new Type();
        Operation operation = new Operation();
        operation.setType(testType);
        assertEquals(testType, operation.getType());
    }

    @Test
    void testSetAndGetUnit() {
        Unit testUnit = new Unit();
        Operation operation = new Operation();
        operation.setUnit(testUnit);
        assertEquals(testUnit, operation.getUnit());
    }

    @Test
    void testSetAndGetCurrency() {
        Currency testCurrency = new Currency();
        Operation operation = new Operation();
        operation.setCurrency(testCurrency);
        assertEquals(testCurrency, operation.getCurrency());
    }

    @Test
    void testSetAndGetDate() {
        Operation operation = new Operation();
        Date testDate = new Date();
        operation.setDate(testDate);
        assertEquals(testDate, operation.getDate());
    }

    @Test
    void testSetAndGetQuantity() {
        Operation operation = new Operation();
        Double testQuantity = 1234567.123456789;
        operation.setQuantity(testQuantity);
        assertEquals(testQuantity, operation.getQuantity());
    }

    @Test
    void testSetAndGetPrice() {
        Operation operation = new Operation();
        BigDecimal testPrice = BigDecimal.valueOf(123.45);
        operation.setPrice(testPrice);
        assertEquals(testPrice, operation.getPrice());
    }

    @Test
    void testSetAndGetCost() {
        Operation operation = new Operation();
        BigDecimal testCost = BigDecimal.valueOf(123.45);
        operation.setCost(testCost);
        assertEquals(testCost, operation.getCost());
    }

    @Test
    void testSetAndGetComment() {
        Operation operation = new Operation();
        String testComment = "testTEST";
        operation.setComment(testComment);
        assertEquals(testComment, operation.getComment());
    }

    @Test
    void testConstructorWithParameters() {
        String testNameOperation = "testOperation";
        User testUser = new User();
        Currency testCurrency = new Currency();
        Type testType = new Type();
        Unit testUnit = new Unit();
        Date testDate = new Date();
        BigDecimal testCost = new BigDecimal("75");
        BigDecimal testPrice = new BigDecimal("25");
        Double testQuantity = 3D;
        String testComment = "This is a test comment";

        Operation testOperationConstructor = new Operation(testNameOperation, testUser, testType, testUnit, testCurrency, testDate, testQuantity, testPrice, testCost, testComment);

        assertEquals(testNameOperation, testOperationConstructor.getOperation());
        assertEquals(testUser, testOperationConstructor.getUser());
        assertEquals(testUnit, testOperationConstructor.getUnit());
        assertEquals(testCurrency, testOperationConstructor.getCurrency());
        assertEquals(testType, testOperationConstructor.getType());
        assertEquals(testDate, testOperationConstructor.getDate());
        assertEquals(testCost, testOperationConstructor.getCost());
        assertEquals(testPrice, testOperationConstructor.getPrice());
        assertEquals(testQuantity, testOperationConstructor.getQuantity());
        assertEquals(testComment, testOperationConstructor.getComment());
    }

    @Test
    void testToString() {
        String testNameOperation = "testOperation";
        User testUser = new User();
        Currency testCurrency = new Currency();
        Type testType = new Type();
        Unit testUnit = new Unit();
        BigDecimal testCost = new BigDecimal("75");
        BigDecimal testPrice = new BigDecimal("25");
        Double testQuantity = 3D;
        String testComment = "This is a test comment";
        Date testDate=new Date();

        Operation testToStringOperation = new Operation(testNameOperation, testUser, testType, testUnit, testCurrency, new Date(), testQuantity, testPrice, testCost, testComment);
        testToStringOperation.setId(1L);
        testToStringOperation.setDate(testDate);

        System.out.println(testToStringOperation);
        String expected = "Operation{id=1, " +
                "operation='testOperation', " +
                "user=User{id=null, tgId='null', " +
                "username='null', " +
                "firstName='null', " +
                "lastName='null', " +
                "registrationDate=null, " +
                "email='null', " +
                "phoneNumber='null'}, " +
                "type=Type{id=null, " +
                "name='null'}, " +
                "unit=Unit{id=null, " +
                "name='null'}, " +
                "currency=Currency{id=null, " +
                "name='null'}, " +
                "date=" + testDate + ", " +
                "quantity=3.0, price=25, cost=75, " +
                "comment='This is a test comment'}\n";
        assertEquals(expected.trim(), testToStringOperation.toString().trim());
    }
}