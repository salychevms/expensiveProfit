package ru.salychev.expensiveprofit.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    public void testGetId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetUsername() {
        User user = new User();
        user.setUsername("testUser");
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testConstructorWithIdAndUsername() {
        User user = new User("testUser", new Date());
        Long userId = user.getId();
        assertEquals(userId, user.getId());
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testSetAndGetFirstName() {
        User user = new User();
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        User user = new User();
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testSetAndGetRegistrationDate() {
        User user = new User();
        user.setRegistrationDate(new Date());
        assertNotNull(user.getRegistrationDate());
    }

    @Test
    public void testSetAndGetEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    public void testSetAndGetPhoneNumber() {
        User user = new User();
        user.setPhoneNumber("123456789");
        assertEquals("123456789", user.getPhoneNumber());
    }

    @Test
    public void testSetAndGetTgId() {
        User user = new User();
        user.setTgId("123456789");
        assertEquals("123456789", user.getTgId());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@test.com");
        user.setPhoneNumber("123456789");
        user.setRegistrationDate(new Date());
        user.setTgId("123456789");

        String expected = "User{id=1, tgId='123456789', username='testUser', firstName='John', " +
                "lastName='Doe', registrationDate=" + user.getRegistrationDate() +
                ", email='test@test.com', phoneNumber='123456789'}";

        assertEquals(expected, user.toString());
    }
}